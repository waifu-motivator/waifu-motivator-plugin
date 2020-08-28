package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import zd.zero.waifu.motivator.plugin.assets.AssetCategory.VISUAL

object VisualAssetManager : RemoteAssetManager<VisualMotivationAssetDefinition, VisualMotivationAsset>(
    VISUAL
) {
    override fun convertToAsset(
        asset: VisualMotivationAssetDefinition,
        assetUrl: String
    ): VisualMotivationAsset =
        asset.toAsset(assetUrl)

    override fun convertToDefinitions(defJson: String): List<VisualMotivationAssetDefinition> =
        Gson().fromJson<List<VisualMotivationAssetDefinition>>(
            defJson, object : TypeToken<List<VisualMotivationAssetDefinition>>() {}.type
        )
}
