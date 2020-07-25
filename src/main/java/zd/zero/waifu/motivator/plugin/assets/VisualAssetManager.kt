package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import zd.zero.waifu.motivator.plugin.assets.AssetCategory.VISUAL
import zd.zero.waifu.motivator.plugin.assets.AssetManager.resolveAssetUrl
import zd.zero.waifu.motivator.plugin.remote.RestClient
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

object VisualAssetManager {

    private lateinit var remoteAssets: List<VisualMotivationAssetDefinition>

    fun supplyAssetDefinitions(): List<VisualMotivationAssetDefinition> =
        if (this::remoteAssets.isInitialized) {
            remoteAssets
        } else {
            val assetUrl = resolveAssetUrl(VISUAL, "assets.json")
            remoteAssets = initializeRemoteAssets(assetUrl)
            remoteAssets
        }

    fun resolveAsset(visualAsset: VisualMotivationAssetDefinition): VisualMotivationAssetDefinition {
        val assetUrl = resolveAssetUrl(VISUAL, visualAsset.imagePath)
        return visualAsset.copy(imagePath = assetUrl)
    }

    private fun initializeRemoteAssets(assetUrl: String): List<VisualMotivationAssetDefinition> =
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

    private fun convertToDefinitions(defJson: String): List<VisualMotivationAssetDefinition> =
        Gson().fromJson<List<VisualMotivationAssetDefinition>>(
            defJson, object : TypeToken<List<VisualMotivationAssetDefinition>>() {}.type
        )

    private fun readLocalFile(assetUrl: String) =
        Files.readAllBytes(Paths.get(URI(assetUrl))).toOptional()
            .map { String(it, Charsets.UTF_8) }

    private val backupAssets = listOf(
        VisualMotivationAssetDefinition(
            "caramelldansen.gif",
            "Caramelldansen",
            arrayOf(
                WaifuAssetCategory.CELEBRATION
            )
        ),
        VisualMotivationAssetDefinition(
            "kill-la-kill-caramelldansen.gif",
            "Caramelldansen",
            arrayOf(
                WaifuAssetCategory.CELEBRATION
            )
        )
    )
}
