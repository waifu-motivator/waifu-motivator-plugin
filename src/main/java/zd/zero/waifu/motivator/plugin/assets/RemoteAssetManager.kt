package zd.zero.waifu.motivator.plugin.assets

import zd.zero.waifu.motivator.plugin.tools.doOrElse
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.Optional.ofNullable

abstract class RemoteAssetManager<T : AssetDefinition>(
    private val assetCategory: AssetCategory
) {
    private lateinit var remoteAssets: List<T>

    init {
        AssetManager.resolveAssetUrl(assetCategory, "assets.json")
            .flatMap { assetUrl -> initializeRemoteAssets(assetUrl) }
            .doOrElse({
                remoteAssets = it
            }) {
                // The plugin cannot work if there is not asset metadata locally :(
                // todo: let user know in bad state....
            }
    }

    fun supplyAssetDefinitions(): List<T> =
        remoteAssets

    fun supplyLocalAssetDefinitions(): List<T> =
        remoteAssets

    abstract fun applyAssetUrl(asset: T, assetUrl: String): T

    fun resolveAsset(asset: T): Optional<T> =
        AssetManager.resolveAssetUrl(assetCategory, asset.path)
            .map { assetUrl -> applyAssetUrl(asset, assetUrl) }

    private fun initializeRemoteAssets(assetUrl: String): Optional<List<T>> =
        try {
            if (assetUrl.startsWith("file://")) {
                readLocalFile(assetUrl)
            } else {
                Optional.empty()
            }.map {
                convertToDefinitions(it)
            }
        } catch (e: Throwable) {
            // todo: log error
            Optional.empty()
        }

    private fun readLocalFile(assetUrl: String) =
        ofNullable(Files.readAllBytes(Paths.get(URI(assetUrl))))
            .map { String(it, Charsets.UTF_8) }

    protected abstract fun convertToDefinitions(defJson: String): List<T>
}
