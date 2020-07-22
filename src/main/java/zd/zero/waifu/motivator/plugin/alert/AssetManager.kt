package zd.zero.waifu.motivator.plugin.alert

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import org.apache.commons.io.IOUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.TimeUnit

private enum class AssetChangedStatus {
    SAME, DIFFERENT, LUL_DUNNO
}

object AssetManager {
    const val ASSETS_SOURCE = "https://waifu-motivation-assets.s3.amazonaws.com"
    const val VISUAL_ASSET_DIRECTORY = "visuals"

    private val httpClient = HttpClients.custom()
        .setUserAgent(ApplicationInfoEx.getInstance().fullApplicationName)
        .build()

    private val log = Logger.getInstance(this::class.java)


    fun resolveAssetUrl(localAssetPath: Path, remoteAssetUrl: String): Optional<String> {
        return canWriteAssetsLocally()
            .flatMap {
                if(hasAssetChanged(localAssetPath, remoteAssetUrl)) {
                    downloadAsset(localAssetPath, remoteAssetUrl)
                } else {
                    localAssetPath.toOptional()
                }
            }.map { it.toUri().toString() }
    }

    fun constructLocalAssetPath(
        assetCategory: String,
        assetPath: String
    ): Optional<Path> =
        getLocalAssetDirectory()
            .map { localInstallDirectory ->
                Paths.get(
                        localInstallDirectory, assetCategory, assetPath
                ).normalize().toAbsolutePath()
            }

    private fun hasAssetChanged(
            localInstallPath: Path,
            remoteAssetUrl: String
    ): Boolean =
        !Files.exists(localInstallPath) ||
            (!hasBeenCheckedToday(localInstallPath) &&
                isLocalDifferentFromRemote(localInstallPath, remoteAssetUrl) == AssetChangedStatus.DIFFERENT)

    private fun hasBeenCheckedToday(localInstallPath: Path): Boolean {
        return false // todo:
    }

    private fun downloadAsset(
            localAssetPath: Path,
            remoteAssetUrl: String
    ): Optional<Path> {
        createDirectories(localAssetPath)
        return downloadRemoteAsset(localAssetPath, remoteAssetUrl)
    }

    private fun canWriteAssetsLocally(): Optional<Boolean> =
        getLocalAssetDirectory()
            .map { Paths.get(it) }
            .filter { Files.isWritable(it.parent) }
            .map { true }

    private fun isLocalDifferentFromRemote(
            localInstallPath: Path,
            remoteAssetUrl: String
    ): AssetChangedStatus =
        getRemoteAssetChecksum(remoteAssetUrl)
            .map {
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
    ): Optional<Path> = try {
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
        } else {
            log.warn("Asset request for $remoteAssetPath responded with $remoteAssetResponse")
        }
        localAssetPath.toOptional()
    } catch (e: Exception) {
        log.error("Unable to get remote remote asset $remoteAssetPath for raisins", e)
        localAssetPath.toOptional()
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
            Paths.get(it, "waifuMotivatorAssets").toAbsolutePath().toString()
        }
}
