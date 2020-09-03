package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.tools.doOrElse

class IdlePersonalityCore : PersonalityCore {

    private var isEventDisplayed = false

    override fun processMotivationEvent(
        motivationEvent: MotivationEvent,
        mood: Mood
    ) {
        if (isEventDisplayed.not()) {
            val project = motivationEvent.project
            VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.WAITING)
                .doOrElse({ asset ->
                    isEventDisplayed = true
                    VisualMotivationFactory.constructMotivation(
                            project,
                            asset,
                            motivationEvent.alertConfigurationSupplier()
                    ).setListener {
                        isEventDisplayed = false
                    }.motivate()
                }) {
                    UpdateNotification.sendMessage(
                            "'${motivationEvent.title}' unavailable offline.",
                            ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                            project
                    )
                }
        }
    }
}
