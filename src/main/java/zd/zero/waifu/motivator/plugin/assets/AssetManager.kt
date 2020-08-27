package zd.zero.waifu.motivator.plugin.assets

import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.Logger
import org.apache.commons.io.IOUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import zd.zero.waifu.motivator.plugin.assets.LocalAssetService.hasAssetChanged
import zd.zero.waifu.motivator.plugin.assets.LocalStorageService.createDirectories
import zd.zero.waifu.motivator.plugin.assets.LocalStorageService.getLocalAssetDirectory
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*
import java.util.concurrent.TimeUnit

enum class AssetCategory(val category: String) {
    VISUAL("visuals"), AUDIBLE("audible"), TEXT("text")
}

object AssetManager {
    private const val ASSETS_SOURCE = "https://waifu.assets.unthrottled.io"

    private val httpClient = HttpClients.custom()
        .setUserAgent(ApplicationInfoEx.getInstance().fullApplicationName)
        .build()

    private val log = Logger.getInstance(this::class.java)

    /**
     * Will return a resolvable URL that can be used to reference an asset.
     * If the asset was able to be downloaded on the local machine it will return a
     * file:// url to the local asset. If it was not able to get the asset then it
     * will return empty if the asset is not available locally.
     */
    fun resolveAssetUrl(assetCategory: AssetCategory, assetPath: String): Optional<String> {
        val remoteAssetUrl = constructRemoteAssetUrl(
            assetCategory, assetPath
        )
        return constructLocalAssetPath(assetCategory, assetPath)
            .flatMap {
                resolveTheAssetUrl(it, remoteAssetUrl)
            }
    }

    private fun constructRemoteAssetUrl(
        assetCategory: AssetCategory,
        assetPath: String
    ): String = "$ASSETS_SOURCE/${assetCategory.category}/$assetPath"

    private fun resolveTheAssetUrl(localAssetPath: Path, remoteAssetUrl: String): Optional<String> =
        when {
            hasAssetChanged(localAssetPath, remoteAssetUrl) ->
                downloadAndGetAssetUrl(localAssetPath, remoteAssetUrl)
            Files.exists(localAssetPath) ->
                localAssetPath.toUri().toString().toOptional()
            else -> Optional.empty()
        }

    fun constructLocalAssetPath(
        assetCategory: AssetCategory,
        assetPath: String
    ): Optional<Path> =
        getLocalAssetDirectory()
            .map { localInstallDirectory ->
                Paths.get(
                    localInstallDirectory, assetCategory.category, assetPath
                ).normalize().toAbsolutePath()
            }

    private fun downloadAndGetAssetUrl(
        localAssetPath: Path,
        remoteAssetUrl: String
    ): Optional<String> {
        createDirectories(localAssetPath)
        val remoteAssetRequest = createGetRequest(remoteAssetUrl)
        return try {
            log.warn("Attempting to download asset $remoteAssetUrl")
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
                log.warn("Asset request for $remoteAssetUrl responded with $remoteAssetResponse")
                Optional.empty()
            }
        } catch (e: Throwable) {
            log.error("Unable to get remote remote asset $remoteAssetUrl for raisins", e)
            Optional.empty()
        } finally {
            remoteAssetRequest.releaseConnection()
        }
    }

    private fun createGetRequest(remoteUrl: String): HttpGet {
        val remoteAssetRequest = HttpGet(remoteUrl)
        remoteAssetRequest.config = RequestConfig.custom()
            .setConnectTimeout(TimeUnit.MILLISECONDS.convert(5L, TimeUnit.SECONDS).toInt())
            .build()
        return remoteAssetRequest
    }
}
