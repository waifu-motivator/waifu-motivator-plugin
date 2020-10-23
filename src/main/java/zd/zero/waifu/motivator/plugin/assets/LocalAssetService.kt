package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import zd.zero.waifu.motivator.plugin.tools.RestClient
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

private enum class AssetChangedStatus {
    SAME, DIFFERENT, LUL_DUNNO
}

object LocalAssetService {
    private val log = Logger.getInstance(this::class.java)
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val assetChecks: ConcurrentHashMap<String, Instant> = readPreviousAssetChecks()

    fun hasAssetChanged(
        localInstallPath: Path,
        remoteAssetUrl: String
    ): Boolean =
        !Files.exists(localInstallPath) ||
            (
                !hasBeenCheckedToday(localInstallPath) &&
                    isLocalDifferentFromRemote(localInstallPath, remoteAssetUrl) == AssetChangedStatus.DIFFERENT
                )

    private fun getOnDiskCheckSum(localAssetPath: Path): String =
        computeCheckSum(Files.readAllBytes(localAssetPath))

    private fun computeCheckSum(byteArray: ByteArray): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(byteArray)
        return StringUtil.toHexString(messageDigest.digest())
    }

    private fun getRemoteAssetChecksum(remoteAssetUrl: String): Optional<String> =
        RestClient.performGet("$remoteAssetUrl.checksum.txt")

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
                    log.warn(
                        """
                      Local asset: $localInstallPath
                      is different from remote asset $remoteAssetUrl
                      Local Checksum: $onDiskCheckSum
                      Remote Checksum: $it
                        """.trimIndent()
                    )
                    AssetChangedStatus.DIFFERENT
                }
            }.orElseGet { AssetChangedStatus.LUL_DUNNO }

    private fun hasBeenCheckedToday(localInstallPath: Path): Boolean =
        assetChecks[getAssetCheckKey(localInstallPath)]?.truncatedTo(ChronoUnit.DAYS) ==
            Instant.now().truncatedTo(ChronoUnit.DAYS)

    private fun writeCheckedDate(localInstallPath: Path) {
        assetChecks[getAssetCheckKey(localInstallPath)] = Instant.now()
        getAssetChecksFile()
            .ifPresent {
                LocalStorageService.createDirectories(it)
                Files.newBufferedWriter(
                    it,
                    Charset.defaultCharset(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                ).use { writer ->
                    writer.write(gson.toJson(assetChecks))
                }
            }
    }

    private fun readPreviousAssetChecks(): ConcurrentHashMap<String, Instant> = try {
        getAssetChecksFile()
            .filter { Files.exists(it) }
            .map {
                Files.newBufferedReader(it).use { reader ->
                    gson.fromJson<ConcurrentHashMap<String, Instant>>(
                        reader,
                        object : TypeToken<ConcurrentHashMap<String, Instant>>() {}.type
                    )
                }
            }.orElseGet { ConcurrentHashMap() }
    } catch (e: Throwable) {
        log.warn("Unable to get local asset checks for raisins", e)
        ConcurrentHashMap()
    }

    private fun getAssetChecksFile() = LocalStorageService.getLocalAssetDirectory()
        .map { Paths.get(it, "assetChecks.json") }

    private fun getAssetCheckKey(localInstallPath: Path) =
        localInstallPath.toAbsolutePath().toString()
}
