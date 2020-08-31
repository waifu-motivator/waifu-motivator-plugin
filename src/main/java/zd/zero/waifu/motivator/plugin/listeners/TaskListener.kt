package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
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

class TaskListener(private val project: Project) : ProjectTaskListener {

    private var previousTaskStatus = TaskStatus.UNKNOWN

    // todo: show task failure for project.
    override fun finished(result: ProjectTaskManager.Result) {
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
            previousTaskStatus == TaskStatus.FAIL -> {
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
            else -> {
                previousTaskStatus = TaskStatus.PASS
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
