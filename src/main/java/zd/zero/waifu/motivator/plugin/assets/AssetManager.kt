package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import org.apache.commons.io.IOUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import zd.zero.waifu.motivator.plugin.tools.RestClient
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

private enum class AssetChangedStatus {
    SAME, DIFFERENT, LUL_DUNNO
}

enum class AssetCategory(val category: String) {
    VISUAL("visuals")
}

object AssetManager {
    private const val ASSETS_SOURCE = "https://waifu.assets.unthrottled.io"

    private val httpClient = HttpClients.custom()
        .setUserAgent(ApplicationInfoEx.getInstance().fullApplicationName)
        .build()

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val log = Logger.getInstance(this::class.java)

    private val assetChecks: MutableMap<String, Instant> = getAssetChecks()

    fun resolveAssetUrl(assetCategory: AssetCategory, assetPath: String): String {
        val remoteAssetUrl = constructRemoteAssetUrl(
            assetCategory, assetPath
        )
        return constructLocalAssetPath(assetCategory, assetPath)
            .flatMap {
                resolveTheAssetUrl(it, remoteAssetUrl)
            }.orElse(remoteAssetUrl)
    }

    private fun constructRemoteAssetUrl(
        assetCategory: AssetCategory,
        assetPath: String
    ): String = "$ASSETS_SOURCE/${assetCategory.category}/$assetPath"

    private fun resolveTheAssetUrl(localAssetPath: Path, remoteAssetUrl: String): Optional<String> =
        if (hasAssetChanged(localAssetPath, remoteAssetUrl)) {
            downloadAsset(localAssetPath, remoteAssetUrl)
        } else {
            localAssetPath.toUri().toString().toOptional()
        }

    private fun constructLocalAssetPath(
        assetCategory: AssetCategory,
        assetPath: String
    ): Optional<Path> =
        getLocalAssetDirectory()
            .map { localInstallDirectory ->
                Paths.get(
                    localInstallDirectory, assetCategory.category, assetPath
                ).normalize().toAbsolutePath()
            }

    private fun getAssetChecks(): MutableMap<String, Instant> = try {
        getAssetChecksFile()
            .filter { Files.exists(it) }
            .map {
                Files.newBufferedReader(it).use { reader ->
                    gson.fromJson<Map<String, Instant>>(
                        reader,
                        object : TypeToken<Map<String, Instant>>() {}.type
                    )
                }.toMutableMap()
            }.orElseGet { mutableMapOf() }
    } catch (e: Throwable) {
        log.warn("Unable to get local asset checks for raisins", e)
        mutableMapOf()
    }

    private fun getAssetChecksFile() = getLocalAssetDirectory()
        .map { Paths.get(it, "assetChecks.json") }

    private fun hasAssetChanged(
        localInstallPath: Path,
        remoteAssetUrl: String
    ): Boolean =
        !Files.exists(localInstallPath) ||
            (!hasBeenCheckedToday(localInstallPath) &&
                isLocalDifferentFromRemote(localInstallPath, remoteAssetUrl) == AssetChangedStatus.DIFFERENT)

    private fun hasBeenCheckedToday(localInstallPath: Path): Boolean =
        assetChecks[getAssetCheckKey(localInstallPath)]?.truncatedTo(ChronoUnit.DAYS) ==
            Instant.now().truncatedTo(ChronoUnit.DAYS)

    private fun downloadAsset(
        localAssetPath: Path,
        remoteAssetUrl: String
    ): Optional<String> {
        createDirectories(localAssetPath)
        return downloadRemoteAsset(localAssetPath, remoteAssetUrl)
    }

    private fun isLocalDifferentFromRemote(
        localInstallPath: Path,
        remoteAssetUrl: String
    ): AssetChangedStatus =
        getRemoteAssetChecksum(remoteAssetUrl)
            .map {
                writeCheckedDate(localInstallPath)
                val onDiskCheckSum = getOnDiskCheckSum(localInstallPath)
                if (it == onDiskCheckSum) {
                    AssetChangedStatus.SAME
                } else {
                    log.warn("""
                      Local asset: $localInstallPath
                      is different from remote asset $remoteAssetUrl
                      Local Checksum: $onDiskCheckSum
                      Remote Checksum: $it
                    """.trimIndent())
                    AssetChangedStatus.DIFFERENT
                }
            }.orElseGet { AssetChangedStatus.LUL_DUNNO }

    private fun writeCheckedDate(localInstallPath: Path) {
        assetChecks[getAssetCheckKey(localInstallPath)] = Instant.now()
        getAssetChecksFile()
            .ifPresent {
                createDirectories(it)
                Files.newBufferedWriter(
                    it, Charset.defaultCharset(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                ).use { writer ->
                    writer.write(gson.toJson(assetChecks))
                }
            }
    }

    private fun getOnDiskCheckSum(localAssetPath: Path): String =
        computeCheckSum(Files.readAllBytes(localAssetPath))

    private fun computeCheckSum(byteArray: ByteArray): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(byteArray)
        return StringUtil.toHexString(messageDigest.digest())
    }

    private fun getRemoteAssetChecksum(remoteAssetUrl: String): Optional<String> =
        RestClient.performGet("$remoteAssetUrl.checksum.txt")

    private fun downloadRemoteAsset(
        localAssetPath: Path,
        remoteAssetPath: String
    ): Optional<String> = try {
        log.warn("Attempting to download asset $remoteAssetPath")
        val remoteAssetRequest = createGetRequest(remoteAssetPath)
        val remoteAssetResponse = httpClient.execute(remoteAssetRequest)
        if (remoteAssetResponse.statusLine.statusCode == 200) {
            remoteAssetResponse.entity.content.use { inputStream ->
                Files.newOutputStream(
                    localAssetPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                ).use { bufferedWriter ->
                    IOUtils.copy(inputStream, bufferedWriter)
                }
            }
            localAssetPath.toUri().toString().toOptional()
        } else {
            log.warn("Asset request for $remoteAssetPath responded with $remoteAssetResponse")
            remoteAssetPath.toOptional()
        }
    } catch (e: Throwable) {
        log.error("Unable to get remote remote asset $remoteAssetPath for raisins", e)
        remoteAssetPath.toOptional()
    }

    private fun createGetRequest(remoteUrl: String): HttpGet {
        val remoteAssetRequest = HttpGet(remoteUrl)
        remoteAssetRequest.config = RequestConfig.custom()
            .setConnectTimeout(TimeUnit.MILLISECONDS.convert(5L, TimeUnit.SECONDS).toInt())
            .build()
        return remoteAssetRequest
    }

    private fun createDirectories(directoriesToCreate: Path) {
        try {
            Files.createDirectories(directoriesToCreate.parent)
        } catch (e: IOException) {
            log.error("Unable to create directories $directoriesToCreate for raisins", e)
        }
    }

    private fun getLocalAssetDirectory(): Optional<String> =
        Optional.ofNullable(
            PathManager.getConfigPath()
        ).map {
            Paths.get(it, "waifuMotivationAssets").toAbsolutePath().toString()
        }

    private fun getAssetCheckKey(localInstallPath: Path) =
        localInstallPath.toAbsolutePath().toString()
}
