package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.tools.AssetTools

class TaskPersonalityCore : PersonalityCore {
    override fun processMotivationEvent(
            motivationEvent: MotivationEvent,
            emotionalState: EmotionalState
    ) {
        val project = motivationEvent.project
        AssetTools.attemptToShowCategories(
                project,
                motivationEvent.alertConfigurationSupplier,
                {
                    UpdateNotification.sendMessage(
                            "'${motivationEvent.title}' Unavailable Offline",
                            ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                            project
                    )
                },
                *getRelevantCategories(motivationEvent, emotionalState)
        )
    }

    private fun getRelevantCategories(
            motivationEvent: MotivationEvent,
            emotionalState: EmotionalState
    ): Array<out WaifuAssetCategory> {
        return when (motivationEvent.category) {
            MotivationEventCategory.POSITIVE -> arrayOf(
                    WaifuAssetCategory.CELEBRATION
            )
            MotivationEventCategory.NEGATIVE -> arrayOf(
                    WaifuAssetCategory.DISAPPOINTMENT,
                    WaifuAssetCategory.SHOCKED
            )
            MotivationEventCategory.NEUTRAL -> arrayOf()
        }
    }
}
