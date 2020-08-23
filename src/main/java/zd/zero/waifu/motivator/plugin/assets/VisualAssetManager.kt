package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import zd.zero.waifu.motivator.plugin.assets.AssetCategory.VISUAL

object VisualAssetManager : RemoteAssetManager<VisualMotivationAssetDefinition>(
    VISUAL,
    listOf(
        VisualMotivationAssetDefinition(
            "caramelldansen.gif",
            "caramelldansen.gif",
            "Caramelldansen",
            ImageDimension(320, 240),
            arrayOf(
                WaifuAssetCategory.CELEBRATION
            )
        ),
        VisualMotivationAssetDefinition(
            "kill-la-kill-caramelldansen.gif",
            "kill-la-kill-caramelldansen.gif",
            "Caramelldansen",
            ImageDimension(250, 184),
            arrayOf(
                WaifuAssetCategory.CELEBRATION
            )
        )
    )
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
