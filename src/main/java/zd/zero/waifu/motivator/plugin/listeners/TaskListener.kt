package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.build.BuildProgressListener
import com.intellij.build.events.BuildEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationEvent
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

class TaskListener : ExternalSystemTaskNotificationListener {
    private val LOGGER = Logger.getInstance(TaskListener::class.java)

    private var didFail = false

    override fun onSuccess(id: ExternalSystemTaskId) {
        if(didFail) {
            didFail = false
//            VisualMotivationFactory.constructMotivation(
//                ProjectManager.getInstance().defaultProject,
//                VisualMotivationAssetProvider.pickAssetFromCategories(
//                    WaifuAssetCategory.CELEBRATION,
//                    WaifuAssetCategory.SMUG
//                ),
//                createAlertConfiguration()
//            )
        }
    }

    override fun onFailure(id: ExternalSystemTaskId, e: Exception) {
        didFail = true
//        VisualMotivationFactory.constructMotivation(
//            ProjectManager.getInstance().defaultProject,
//            VisualMotivationAssetProvider.pickAssetFromCategories(
//                WaifuAssetCategory.DISAPPOINTMENT,
//                WaifuAssetCategory.SHOCKED
//            ),
//            createAlertConfiguration()
//        ).motivate()
    }

    private fun createAlertConfiguration(): AlertConfiguration {
        val pluginState = WaifuMotivatorPluginState.getInstance().state
        return AlertConfiguration(
            pluginState!!.isUnitTesterMotivationEnabled || pluginState.isUnitTesterMotivationSoundEnabled,
            pluginState.isUnitTesterMotivationEnabled,
            pluginState.isUnitTesterMotivationSoundEnabled
        )
    }


    override fun onTaskOutput(id: ExternalSystemTaskId, text: String, stdOut: Boolean) {
//        LOGGER.warn("""
//            $id
//            $text
//        """.trimIndent())
    }

    override fun onStatusChange(event: ExternalSystemTaskNotificationEvent) {
//        println(event)
    }

    override fun onCancel(id: ExternalSystemTaskId) {}

    override fun onEnd(id: ExternalSystemTaskId) {}

    override fun beforeCancel(id: ExternalSystemTaskId) {}

    override fun onStart(id: ExternalSystemTaskId) {}

}

class ProgressListener: BuildProgressListener {
    override fun onEvent(buildId: Any, event: BuildEvent) {
        println(event.toString())
    }

}
