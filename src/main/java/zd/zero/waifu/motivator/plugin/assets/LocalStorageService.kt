package zd.zero.waifu.motivator.plugin.assets

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.tools.ExceptionTools.runSafely
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Optional

object LocalStorageService {
    private val log = Logger.getInstance(this::class.java)

    fun createDirectories(directoriesToCreate: Path) {
        try {
            Files.createDirectories(directoriesToCreate.parent)
        } catch (e: IOException) {
            log.error("Unable to create directories $directoriesToCreate for raisins", e)
        }
    }

    private const val ASSET_DIRECTORY = "waifuMotivationAssets"

    fun getLocalAssetDirectory(): Optional<String> {
        return Optional.ofNullable(
            PathManager.getConfigPath()
        ).map {
            Paths.get(it, ASSET_DIRECTORY).toAbsolutePath().toString()
        }
    }

    fun getLocalAssetDirectoryNoOptional(): String {
        return Paths.get(
            PathManager.getConfigPath(),
            ASSET_DIRECTORY
        ).toAbsolutePath().toString()
    }

    fun getGlobalAssetDirectory(): Optional<String> =
        Paths.get(
            PathManager.getConfigPath(),
            "..",
            ASSET_DIRECTORY
        ).toAbsolutePath()
            .normalize()
            .toOptional()
            .filter { Files.isWritable(it.parent) }
            .map {
                if (Files.exists(it).not()) {
                    runSafely({
                        Files.createDirectories(it)
                    }) { throwable: Throwable ->
                        log.warn("Unable to create global directory for raisins", throwable)
                    }
                }
                it.toString()
            }
}
