package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.project.ProjectManager
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskManager
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.AssetTools

internal enum class TaskStatus {
    PASS, FAIL, UNKNOWN
}

class TaskListener : ProjectTaskListener {

    private var previousTaskStatus = TaskStatus.UNKNOWN

    override fun finished(result: ProjectTaskManager.Result) {
        val project = ProjectManager.getInstance().defaultProject
        when {
            result.hasErrors() -> {
                AssetTools.attemptToShowCategories(
                    project,
                    { createAlertConfiguration() },
                    {
                        UpdateNotification.sendMessage(
                            "'Task Failure Motivation' Unavailable Offline",
                            "Unfortunately I wasn't able to find any waifu saved locally. Please try again " +
                                "when you are back online!",
                            project
                        )
                    },
                    WaifuAssetCategory.DISAPPOINTMENT,
                    WaifuAssetCategory.SHOCKED
                )
                previousTaskStatus = TaskStatus.FAIL
            }
            previousTaskStatus == TaskStatus.FAIL -> {
                AssetTools.attemptToShowCategories(
                    project,
                    { createAlertConfiguration() },
                    {
                        UpdateNotification.sendMessage(
                            "'Task Success Motivation' Unavailable Offline",
                            "Unfortunately I wasn't able to find any waifu saved locally. Please try again " +
                                "when you are back online!",
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
