package zd.zero.waifu.motivator.plugin.alert

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import zd.zero.waifu.motivator.plugin.alert.AssetManager.ASSETS_SOURCE
import zd.zero.waifu.motivator.plugin.alert.AssetManager.VISUAL_ASSET_DIRECTORY

object AssetDefinitionService {
    fun getRandomCelebrationVisualAsset(): VisualMotivationAssetDefinition {
        val celebrationAsset = getCelebrationVisualAssets().random()
        return AssetManager.getVisualAsset(celebrationAsset)
    }

    fun getRandomCelebrationAudibleAsset(): AudibleMotivationAssetDefinition {
        // todo: use remote/local assets
        val soundFile = listOf("nice_nice_nice_nice.wav", "good_job.wav", "waoow.mp3").random()
        return AudibleMotivationAssetDefinition(
            soundFile,
            arrayOf(WaifuAssetCategory.CELEBRATION)
        )
    }

    fun getRandomCelebrationTextualAsset(): TextualMotivationAssetDefinition {
        // todo: use remote/local assets
        val messages = listOf("Excellent!", "You did it!", "Way to go!", "Amazing work!", "You're the best!")
        val title = messages.random()
        return TextualMotivationAssetDefinition(
            title,
            messages.random(),
            arrayOf(WaifuAssetCategory.CELEBRATION)
        )
    }

    private fun getCelebrationVisualAssets(): List<VisualMotivationAssetDefinition> {
        return VisualAssetSupplier.supplyVisualAssets().filter {
            it.categories.contains(WaifuAssetCategory.CELEBRATION)
        }
    }

}

object VisualAssetSupplier {

    private lateinit var remoteAssets: List<VisualMotivationAssetDefinition>

    fun supplyVisualAssets(): List<VisualMotivationAssetDefinition> =
        if (this::remoteAssets.isInitialized) {
            remoteAssets
        } else {
            remoteAssets = RestClient.performGet("$ASSETS_SOURCE/$VISUAL_ASSET_DIRECTORY/assets.json")
                .map {
                    Gson().fromJson<List<VisualMotivationAssetDefinition>>(
                        it, object : TypeToken<List<VisualMotivationAssetDefinition>>() {}.type
                    )
                }.orElseGet {
                    listOf(
                        VisualMotivationAssetDefinition(
                            "caramelldansen.gif",
                            "Caramelldansen",
                            arrayOf(
                                WaifuAssetCategory.CELEBRATION
                            )
                        ),
                        VisualMotivationAssetDefinition(
                            "kill-la-kill-caramelldansen.gif",
                            "Caramelldansen",
                            arrayOf(
                                WaifuAssetCategory.CELEBRATION
                            )
                        )
                    )
                }
            remoteAssets
        }
}
