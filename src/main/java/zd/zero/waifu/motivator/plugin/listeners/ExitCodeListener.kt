package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.PluginSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState.Companion.DEFAULT_DELIMITER

const val OK_EXIT_CODE = 0
const val FORCE_KILLED_EXIT_CODE = 130

fun String.toExitCodes(): Set<Int> = this.split(DEFAULT_DELIMITER)
    .filter { it.isNotEmpty() }
    .map { it.trim().toInt() }.toSet()

class ExitCodeListener(private val project: Project) : Runnable, Disposable {
    private val log = Logger.getInstance(javaClass)
    private val messageBus = ApplicationManager.getApplication().messageBus.connect()

    private var allowedExitCodes = WaifuMotivatorPluginState.getPluginState()
        .allowedExitCodes.toExitCodes()
    init {
        messageBus.subscribe(
            PluginSettingsListener.PLUGIN_SETTINGS_TOPIC,
            PluginSettingsListener { newPluginState ->
                allowedExitCodes = newPluginState
                    .allowedExitCodes.toExitCodes()
            }
        )

        messageBus.subscribe(
            ExecutionManager.EXECUTION_TOPIC,
            object : ExecutionListener {
                override fun processTerminated(
                    executorId: String,
                    env: ExecutionEnvironment,
                    handler: ProcessHandler,
                    exitCode: Int
                ) {
                    log.debug("Observed exit code of $exitCode")
                    if (allowedExitCodes.contains(exitCode).not() && env.project == project) {
                        run()
                    }
                }
            }
        )
    }

    override fun dispose() {
        messageBus.dispose()
    }

    override fun run() {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(MotivationEventListener.TOPIC)
            .onEventTrigger(
                MotivationEvent(
                    MotivationEvents.TASK,
                    MotivationEventCategory.NEGATIVE,
                    "Exit Code Motivation",
                    project
                ) { createAlertConfiguration() }
            )
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
