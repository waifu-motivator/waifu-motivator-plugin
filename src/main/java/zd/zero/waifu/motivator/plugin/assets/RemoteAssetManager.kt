package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import zd.zero.waifu.motivator.plugin.tools.RestClient
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

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

    private fun convertToDefinitions(defJson: String): List<T> =
        Gson().fromJson<List<T>>(
            defJson, object : TypeToken<List<T>>() {}.type
        )

    private fun readLocalFile(assetUrl: String) =
        Files.readAllBytes(Paths.get(URI(assetUrl))).toOptional()
            .map { String(it, Charsets.UTF_8) }
}
