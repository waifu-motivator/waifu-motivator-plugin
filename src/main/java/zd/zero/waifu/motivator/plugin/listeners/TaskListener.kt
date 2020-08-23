package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.project.ProjectManager
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskManager
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

internal enum class TaskStatus {
    PASS, FAIL, UNKNOWN
}

class TaskListener : ProjectTaskListener {

    private var previousTaskStatus = TaskStatus.UNKNOWN

    override fun finished(result: ProjectTaskManager.Result) {
        when {
            result.hasErrors() -> {
                VisualMotivationFactory.constructMotivation(
                    ProjectManager.getInstance().defaultProject,
                    VisualMotivationAssetProvider.pickAssetFromCategories(
                        WaifuAssetCategory.DISAPPOINTMENT,
                        WaifuAssetCategory.SHOCKED
                    ),
                    createAlertConfiguration()
                ).motivate()
                previousTaskStatus = TaskStatus.FAIL
            }
            previousTaskStatus == TaskStatus.FAIL -> {
                VisualMotivationFactory.constructMotivation(
                    ProjectManager.getInstance().defaultProject,
                    VisualMotivationAssetProvider.pickAssetFromCategories(
                        WaifuAssetCategory.CELEBRATION,
                        WaifuAssetCategory.CELEBRATION,
                        WaifuAssetCategory.SMUG
                    ),
                    createAlertConfiguration()
                ).motivate()
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
            pluginState!!.isUnitTesterMotivationEnabled || pluginState.isUnitTesterMotivationSoundEnabled,
            pluginState.isUnitTesterMotivationEnabled,
            pluginState.isUnitTesterMotivationSoundEnabled
        )
    }
}
