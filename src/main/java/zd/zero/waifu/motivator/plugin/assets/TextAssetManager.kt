package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TextAssetManager : RemoteAssetManager<TextualMotivationAssetDefinition>(
        AssetCategory.TEXT,
    emptyList()
) {
    override fun applyAssetUrl(
        visualAsset: TextualMotivationAssetDefinition,
        assetUrl: String
    ): TextualMotivationAssetDefinition =
        visualAsset.copy(path = assetUrl)

    override fun convertToDefinitions(defJson: String): List<TextualMotivationAssetDefinition> =
        Gson().fromJson<List<TextualMotivationAssetDefinition>>(
            defJson, object : TypeToken<List<TextualMotivationAssetDefinition>>() {}.type
        )
}
