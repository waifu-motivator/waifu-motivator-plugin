package zd.zero.waifu.motivator.plugin.personality.core

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.MotivationFactory
import zd.zero.waifu.motivator.plugin.motivation.MotivationLifecycleListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.personality.core.emotions.Mood

class IdlePersonalityCore : PersonalityCore {

    private var displayedProjects = mutableSetOf<Project>()

    override fun processMotivationEvent(
        motivationEvent: MotivationEvent,
        mood: Mood
    ) {
        val project = motivationEvent.project
        if (displayedProjects.contains(motivationEvent.project).not()) {
            MotivationFactory.showMotivationEventForCategory(
                motivationEvent,
                object : MotivationLifecycleListener {
                    override fun onDisplay() {
                        displayedProjects.add(project)
                    }

                    override fun onDispose() {
                        displayedProjects.remove(project)
                    }

                },
                WaifuAssetCategory.WAITING
            )
        }
    }
}
