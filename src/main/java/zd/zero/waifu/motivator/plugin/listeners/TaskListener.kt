package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskManager
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

internal enum class TaskStatus {
    PASS, FAIL, UNKNOWN
}

class TaskListener : ProjectTaskListener {

    override fun finished(result: ProjectTaskManager.Result) {
        val project = ProjectManager.getInstance().defaultProject
        when {
            result.hasErrors() -> {
                ApplicationManager.getApplication().messageBus
                    .syncPublisher(MotivationEventListener.TOPIC)
                    .onEventTrigger(
                        MotivationEvent(
                            MotivationEvents.TASK,
                            MotivationEventCategory.NEGATIVE,
                            "Task Failure Motivation",
                            project
                        ) { createAlertConfiguration() }
                    )
            }
            else -> {
                ApplicationManager.getApplication().messageBus
                    .syncPublisher(MotivationEventListener.TOPIC)
                    .onEventTrigger(
                        MotivationEvent(
                            MotivationEvents.TASK,
                            MotivationEventCategory.POSITIVE,
                            "Task Success Motivation",
                            project
                        ) { createAlertConfiguration() }
                    )
            }
        }
    }

    private fun createAlertConfiguration(): AlertConfiguration {
        val pluginState = WaifuMotivatorPluginState.getInstance().state
        return AlertConfiguration(
            pluginState!!.isTaskMotivationEnabled || pluginState.isTaskSoundEnabled,
            pluginState.isTaskMotivationEnabled,
            pluginState.isTaskSoundEnabled
        )
    }
}
