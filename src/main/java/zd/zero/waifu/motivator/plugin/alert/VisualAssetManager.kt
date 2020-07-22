package zd.zero.waifu.motivator.plugin.alert

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
            val remoteAssetUrl = constructRemoteAssetUrl(
                    AssetManager.VISUAL_ASSET_DIRECTORY, "assets.json"
            )
            val assetUrl =
                AssetManager.constructLocalAssetPath(AssetManager.VISUAL_ASSET_DIRECTORY, "assets.json")
                    .flatMap {
                        AssetManager.resolveAssetUrl(it, remoteAssetUrl)
                    }.orElse(remoteAssetUrl)


            remoteAssets =
                if(assetUrl.startsWith("file://")) {
                    Files.readString(Paths.get(URI(assetUrl))).toOptional()
                } else {
                    RestClient.performGet(assetUrl)
                }
                .map {
                    Gson().fromJson<List<VisualMotivationAssetDefinition>>(
                        it, object : TypeToken<List<VisualMotivationAssetDefinition>>() {}.type
                    )
                }.orElseGet {
                    listOf(
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
            remoteAssets
        }


    fun getAsset(visualAsset: VisualMotivationAssetDefinition): VisualMotivationAssetDefinition {
        val remoteAssetUrl = constructRemoteAssetUrl(
                AssetManager.VISUAL_ASSET_DIRECTORY, visualAsset.imagePath
        )
        val assetUrl =
            AssetManager.constructLocalAssetPath(AssetManager.VISUAL_ASSET_DIRECTORY, visualAsset.imagePath)
                .flatMap {
                    AssetManager.resolveAssetUrl(it, remoteAssetUrl)
                }.orElse(remoteAssetUrl)

        return visualAsset.copy(imagePath = assetUrl)
    }

    private fun constructRemoteAssetUrl(
        assetCategory: String,
        assetPath: String
    ): String = "${AssetManager.ASSETS_SOURCE}/$assetCategory/$assetPath"
}
