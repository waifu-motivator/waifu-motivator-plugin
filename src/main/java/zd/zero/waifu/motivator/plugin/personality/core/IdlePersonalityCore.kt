package zd.zero.waifu.motivator.plugin.personality.core

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.personality.core.emotions.Mood
import zd.zero.waifu.motivator.plugin.tools.doOrElse

class IdlePersonalityCore : PersonalityCore {

    private var displayedProjects = mutableSetOf<Project>()

    override fun processMotivationEvent(
        motivationEvent: MotivationEvent,
        mood: Mood
    ) {
        val project = motivationEvent.project
        if (displayedProjects.contains(motivationEvent.project).not()) {
            VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.WAITING)
                .doOrElse({ asset ->
                    displayedProjects.add(project)
                    VisualMotivationFactory.constructMotivation(
                            project,
                            asset,
                            motivationEvent.alertConfigurationSupplier()
                    ).setListener {
                        displayedProjects.remove(project)
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
