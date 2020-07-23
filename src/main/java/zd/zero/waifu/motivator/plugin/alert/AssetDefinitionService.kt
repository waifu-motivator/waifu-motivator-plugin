package zd.zero.waifu.motivator.plugin.alert

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WaifuMotivatorAlertAssetCategoryDeserializer : JsonDeserializer<WaifuMotivatorAlertAssetCategory> {
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): WaifuMotivatorAlertAssetCategory =
        WaifuMotivatorAlertAssetCategory.valueOf(jsonElement.asString.toUpperCase())

}

object AssetDefinitionService {
    fun getRandomCelebrationVisualAsset(): VisualMotivationAssetDefinition {
        val celebrationAsset = getCelebrationVisualAssets().random()
        return VisualAssetManager.getAsset(celebrationAsset)
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
        return VisualAssetManager.supplyAssetDefinitions().filter {
            it.categories.contains(WaifuAssetCategory.CELEBRATION)
        }
    }
}
