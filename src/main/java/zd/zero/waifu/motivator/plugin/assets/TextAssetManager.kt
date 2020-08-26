package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URI
import java.nio.file.Paths

object TextAssetManager : RemoteAssetManager<TextualMotivationAssetDefinition, TextualMotivationAssetPackage>(
        AssetCategory.TEXT
) {
    override fun convertToAsset(
        asset: TextualMotivationAssetDefinition,
        assetUrl: String
    ): TextualMotivationAssetPackage =
        TextualMotivationAssetPackage(Paths.get(URI(assetUrl)))

    override fun convertToDefinitions(defJson: String): List<TextualMotivationAssetDefinition> =
        Gson().fromJson<List<TextualMotivationAssetDefinition>>(
            defJson, object : TypeToken<List<TextualMotivationAssetDefinition>>() {}.type
        )
}
