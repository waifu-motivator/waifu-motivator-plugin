package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URI
import java.nio.file.Paths

object AudibleAssetManager : RemoteAssetManager<AudibleMotivationAssetDefinition, AudibleMotivationAsset>(
    AssetCategory.AUDIBLE
) {
    override fun convertToAsset(
        asset: AudibleMotivationAssetDefinition,
        assetUrl: String
    ): AudibleMotivationAsset =
        AudibleMotivationAsset(Paths.get(URI(assetUrl)))

    override fun convertToDefinitions(defJson: String): List<AudibleMotivationAssetDefinition> =
        Gson().fromJson<List<AudibleMotivationAssetDefinition>>(
            defJson, object : TypeToken<List<AudibleMotivationAssetDefinition>>() {}.type
        )
}
