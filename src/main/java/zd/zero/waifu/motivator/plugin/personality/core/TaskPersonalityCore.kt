package zd.zero.waifu.motivator.plugin.personality.core

import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.MotivationFactory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.personality.core.emotions.Mood
import zd.zero.waifu.motivator.plugin.tools.toArray

class TaskPersonalityCore : PersonalityCore {

    override fun processMotivationEvent(
        motivationEvent: MotivationEvent,
        mood: Mood
    ) {
        MotivationFactory.showMotivationEventFromCategories(
            motivationEvent,
            *getRelevantCategories(motivationEvent, mood)
        )
    }

    private fun getRelevantCategories(
        motivationEvent: MotivationEvent,
        mood: Mood
    ): Array<out WaifuAssetCategory> =
        when (motivationEvent.category) {
            MotivationEventCategory.POSITIVE -> getPositiveMotivationAsset(mood)
            MotivationEventCategory.NEGATIVE -> getNegativeMotivationAsset(mood)
            MotivationEventCategory.NEUTRAL -> arrayOf()
        }

    private fun getPositiveMotivationAsset(mood: Mood): Array<WaifuAssetCategory> {
        return when (mood) {
            Mood.SMUG -> WaifuAssetCategory.SMUG.toArray()
            Mood.HAPPY -> arrayOf(
                WaifuAssetCategory.CELEBRATION, WaifuAssetCategory.HAPPY
            )
            else -> WaifuAssetCategory.CELEBRATION.toArray()
        }
    }

    private fun getNegativeMotivationAsset(mood: Mood): Array<WaifuAssetCategory> {
        return when (mood) {
            Mood.FRUSTRATED -> WaifuAssetCategory.FRUSTRATION.toArray()
            Mood.ENRAGED -> WaifuAssetCategory.ENRAGED.toArray()
            Mood.SHOCKED -> arrayOf(WaifuAssetCategory.SHOCKED)
            else -> arrayOf(
                WaifuAssetCategory.DISAPPOINTMENT,
                WaifuAssetCategory.SHOCKED
            )
        }
    }
}
