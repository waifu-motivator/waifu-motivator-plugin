package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.MessageBundle
import zd.zero.waifu.motivator.plugin.tools.ExceptionTools.runSafely
import java.net.URI
import java.nio.file.Paths
import java.util.Optional

object AudibleAssetManager : RemoteAssetManager<AudibleMotivationAssetDefinition, AudibleMotivationAsset>(
    AssetCategory.AUDIBLE
) {
    private val log = Logger.getInstance(this::class.java)
    override fun convertToAsset(
        asset: AudibleMotivationAssetDefinition,
        assetUrl: String
    ): AudibleMotivationAsset =
        AudibleMotivationAsset(
            Paths.get(URI(assetUrl)),
            asset.groupId
        )

    override fun convertToDefinitions(defJson: String): Optional<List<AudibleMotivationAssetDefinition>> =
        runSafely({
            Gson().fromJson<List<AudibleMotivationAssetDefinition>>(
                defJson,
                object : TypeToken<List<AudibleMotivationAssetDefinition>>() {}.type
            )
        }) {
            log.warn(MessageBundle.message("logs.warn.unable.read.auditable.assets") + "$defJson", it)
        }
}
