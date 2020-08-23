package zd.zero.waifu.motivator.plugin.assets

import zd.zero.waifu.motivator.plugin.tools.RestClient
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Optional.ofNullable

abstract class RemoteAssetManager<T : AssetDefinition>(
    private val assetCategory: AssetCategory,
    private val backupAssets: List<T>
) {
    private lateinit var remoteAssets: List<T>

    fun supplyAssetDefinitions(): List<T> =
        if (this::remoteAssets.isInitialized) {
            remoteAssets
        } else {
            val assetUrl = AssetManager.resolveAssetUrl(assetCategory, "assets.json")
            remoteAssets = initializeRemoteAssets(assetUrl)
            remoteAssets
        }

    abstract fun applyAssetUrl(visualAsset: T, assetUrl: String): T

    fun resolveAsset(visualAsset: T): T {
        val assetUrl = AssetManager.resolveAssetUrl(assetCategory, visualAsset.path)
        return applyAssetUrl(visualAsset, assetUrl)
    }

    private fun initializeRemoteAssets(assetUrl: String): List<T> =
        try {
            if (assetUrl.startsWith("file://")) {
                readLocalFile(assetUrl)
            } else {
                RestClient.performGet(assetUrl)
            }.map {
                convertToDefinitions(it)
            }.orElseGet {
                backupAssets
            }
        } catch (e: Throwable) {
            backupAssets
        }

    private fun readLocalFile(assetUrl: String) =
        ofNullable(Files.readAllBytes(Paths.get(URI(assetUrl))))
            .map { String(it, Charsets.UTF_8) }

    protected abstract fun convertToDefinitions(defJson: String): List<T>
}
