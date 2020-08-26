package zd.zero.waifu.motivator.plugin.assets

import zd.zero.waifu.motivator.plugin.tools.doOrElse
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.Optional.ofNullable

abstract class RemoteAssetManager<T : AssetDefinition, U : Asset>(
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

    // todo: this
    fun supplyLocalAssetDefinitions(): List<T> =
        remoteAssets

    abstract fun convertToAsset(asset: T, assetUrl: String): U

    fun resolveAsset(asset: T): Optional<U> =
        AssetManager.resolveAssetUrl(assetCategory, asset.path)
            .map { assetUrl -> convertToAsset(asset, assetUrl) }

    private fun initializeRemoteAssets(assetUrl: String): Optional<List<T>> =
        try {
            readLocalFile(assetUrl)
                .map {
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
