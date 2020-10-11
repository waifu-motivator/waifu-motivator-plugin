package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.tools.ExceptionTools
import java.net.URI
import java.nio.file.Paths
import java.util.*

object TextAssetManager : RemoteAssetManager<TextualMotivationAssetDefinition, TextualMotivationAssetPackage>(
    AssetCategory.TEXT
) {
    private val log = Logger.getInstance(this::class.java)
    override fun convertToAsset(
        asset: TextualMotivationAssetDefinition,
        assetUrl: String
    ): TextualMotivationAssetPackage =
        TextualMotivationAssetPackage(
            Paths.get(URI(assetUrl)),
            asset.groupId
        )

    override fun convertToDefinitions(defJson: String): Optional<List<TextualMotivationAssetDefinition>> =
        ExceptionTools.runSafely({
            Gson().fromJson<List<TextualMotivationAssetDefinition>>(
                defJson, object : TypeToken<List<TextualMotivationAssetDefinition>>() {}.type
            )
        }) {
            log.warn("Unable to read Text Assets for reasons $defJson", it)
        }
}
