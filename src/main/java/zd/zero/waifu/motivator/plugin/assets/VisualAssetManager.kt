package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.assets.AssetCategory.VISUAL
import zd.zero.waifu.motivator.plugin.tools.ExceptionTools.runSafely
import java.util.*

object VisualAssetManager : RemoteAssetManager<VisualMotivationAssetDefinition, VisualMotivationAsset>(
    VISUAL
) {
    private val log: Logger? = Logger.getInstance(this::class.java)

    override fun convertToAsset(
        asset: VisualMotivationAssetDefinition,
        assetUrl: String
    ): VisualMotivationAsset =
        asset.toAsset(assetUrl)

    override fun convertToDefinitions(defJson: String): Optional<List<VisualMotivationAssetDefinition>> =
        runSafely({
            Gson().fromJson<List<VisualMotivationAssetDefinition>>(
                defJson, object : TypeToken<List<VisualMotivationAssetDefinition>>() {}.type
            )
        }) {
            log?.warn("Unable to read Visual Assets for reasons $defJson", it)
        }
}
