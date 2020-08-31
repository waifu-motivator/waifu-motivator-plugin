package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification.sendMessage
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.doOrElse

class ExitCodeListener(private val project: Project) : Runnable, Disposable {
    private val messageBus = ApplicationManager.getApplication().messageBus.connect()

    init {
        messageBus.subscribe(ExecutionManager.EXECUTION_TOPIC, object : ExecutionListener {
            override fun processTerminated(
                executorId: String,
                env: ExecutionEnvironment,
                handler: ProcessHandler,
                exitCode: Int
            ) {
                if (exitCode > 0 && env.project == project) {
                    run()
                }
            }
        })
    }

    override fun dispose() {
        messageBus.dispose()
    }

    private var isEventDisplayed = false
    override fun run() {
        VisualMotivationAssetProvider.pickAssetFromCategories(
            WaifuAssetCategory.SHOCKED,
            WaifuAssetCategory.DISAPPOINTMENT
        )
            .doOrElse({ asset ->
                isEventDisplayed = true
                VisualMotivationFactory.constructMotivation(
                    project,
                    asset,
                    createAlertConfiguration()
                ).motivate()
            }) {
                sendMessage(
                    "'Exit Code Events' Unavailable Offline",
                    ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                    project
                )
            }
    }

    private fun createAlertConfiguration(): AlertConfiguration {
        val pluginState = WaifuMotivatorPluginState.getInstance().state
        return AlertConfiguration(
            pluginState!!.isExitCodeNotificationEnabled || pluginState.isExitCodeSoundEnabled,
            pluginState.isExitCodeNotificationEnabled,
            pluginState.isExitCodeSoundEnabled
        )
    }
}
