package zd.zero.waifu.motivator.plugin.personality.core

import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.MotivationFactory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.personality.core.emotions.EmotionalMutationAction

class ResetCore {

    fun processMutationEvent(emotionalMutationAction: EmotionalMutationAction) {
        MotivationFactory.showUntitledMotivationEventFromCategories(
            MotivationEvent(
                MotivationEvents.MISC,
                MotivationEventCategory.NEUTRAL,
                "Relax Acknowledgment",
                emotionalMutationAction.project ?: ProjectManager.getInstance().openProjects.first()
            ) {
                AlertConfiguration(
                    isAlertEnabled = true,
                    isDisplayNotificationEnabled = true,
                    isSoundAlertEnabled = false
                )
            },
            WaifuAssetCategory.ACKNOWLEDGEMENT,
            WaifuAssetCategory.ACKNOWLEDGEMENT,
            WaifuAssetCategory.ACKNOWLEDGEMENT,
            WaifuAssetCategory.HAPPY
        )
    }
}
