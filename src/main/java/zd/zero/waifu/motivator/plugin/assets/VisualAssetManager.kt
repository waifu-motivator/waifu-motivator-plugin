package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import zd.zero.waifu.motivator.plugin.assets.AssetCategory.VISUAL

object VisualAssetManager : RemoteAssetManager<VisualMotivationAssetDefinition>(
    VISUAL
) {
    override fun applyAssetUrl(
        visualAsset: VisualMotivationAssetDefinition,
        assetUrl: String
    ): VisualMotivationAssetDefinition =
        visualAsset.copy(path = assetUrl, imagePath = assetUrl)

    override fun convertToDefinitions(defJson: String): List<VisualMotivationAssetDefinition> =
        Gson().fromJson<List<VisualMotivationAssetDefinition>>(
            defJson, object : TypeToken<List<VisualMotivationAssetDefinition>>() {}.type
        )
}
