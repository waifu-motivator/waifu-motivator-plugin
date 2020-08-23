package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AudibleAssetManager : RemoteAssetManager<AudibleMotivationAssetDefinition>(
    AssetCategory.AUDIBLE,
    emptyList()
) {
    override fun applyAssetUrl(
        visualAsset: AudibleMotivationAssetDefinition,
        assetUrl: String
    ): AudibleMotivationAssetDefinition =
        visualAsset.copy(path = assetUrl)

    override fun convertToDefinitions(defJson: String): List<AudibleMotivationAssetDefinition> =
        Gson().fromJson<List<AudibleMotivationAssetDefinition>>(
            defJson, object : TypeToken<List<AudibleMotivationAssetDefinition>>() {}.type
        )
}
