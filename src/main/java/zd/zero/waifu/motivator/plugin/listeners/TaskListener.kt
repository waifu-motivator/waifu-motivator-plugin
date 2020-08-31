package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.project.ProjectManager
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskManager
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.AssetTools.attemptToShowCategories

internal enum class TaskStatus {
    PASS, FAIL, UNKNOWN
}

class TaskListener : ProjectTaskListener {

    private var previousTaskStatus = TaskStatus.UNKNOWN

    override fun finished(result: ProjectTaskManager.Result) {
        val project = ProjectManager.getInstance().defaultProject
        when {
            result.hasErrors() -> {
                attemptToShowCategories(
                    project,
                    { createAlertConfiguration() },
                    {
                        UpdateNotification.sendMessage(
                            "'Task Failure Motivation' Unavailable Offline",
                            ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                            project
                        )
                    },
                    WaifuAssetCategory.DISAPPOINTMENT,
                    WaifuAssetCategory.SHOCKED
                )
                previousTaskStatus = TaskStatus.FAIL
            }
            previousTaskStatus == TaskStatus.FAIL -> {
                attemptToShowCategories(
                    project,
                    { createAlertConfiguration() },
                    {
                        UpdateNotification.sendMessage(
                            "'Task Success Motivation' Unavailable Offline",
                            ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                            project
                        )
                    },
                    WaifuAssetCategory.CELEBRATION,
                    WaifuAssetCategory.CELEBRATION,
                    WaifuAssetCategory.SMUG
                )
                previousTaskStatus = TaskStatus.PASS
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
