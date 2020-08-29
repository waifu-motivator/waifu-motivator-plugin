package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.tools.ExceptionTools
import java.net.URI
import java.nio.file.Paths
import java.util.*

object AudibleAssetManager : RemoteAssetManager<AudibleMotivationAssetDefinition, AudibleMotivationAsset>(
    AssetCategory.AUDIBLE
) {
    private val log = Logger.getInstance(this::class.java)
    override fun convertToAsset(
        asset: AudibleMotivationAssetDefinition,
        assetUrl: String
    ): AudibleMotivationAsset =
        AudibleMotivationAsset(Paths.get(URI(assetUrl)))

    override fun convertToDefinitions(defJson: String): Optional<List<AudibleMotivationAssetDefinition>> =
        ExceptionTools.runSafely({
            Gson().fromJson<List<AudibleMotivationAssetDefinition>>(
                defJson, object : TypeToken<List<AudibleMotivationAssetDefinition>>() {}.type
            )
        }) {
            log.warn("Unable to read Audible Assets for reasons $defJson", it)
        }
}
