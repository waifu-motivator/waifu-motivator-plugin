package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.PluginSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState.Companion.DEFAULT_IDLE_TIMEOUT_IN_MINUTES
import java.util.concurrent.TimeUnit

class IdleEventListener(private val project: Project) : Runnable, Disposable {
    private val messageBus = ApplicationManager.getApplication().messageBus.connect()

    init {
        val self = this
        messageBus.subscribe(
            PluginSettingsListener.PLUGIN_SETTINGS_TOPIC,
            object : PluginSettingsListener {
                override fun settingsUpdated(newPluginState: WaifuMotivatorState) {
                    IdeEventQueue.getInstance().removeIdleListener(self)
                    IdeEventQueue.getInstance().addIdleListener(
                        self,
                        TimeUnit.MILLISECONDS.convert(
                            newPluginState.idleTimeoutInMinutes,
                            TimeUnit.MINUTES
                        ).toInt()
                    )
                }
            }
        )
        IdeEventQueue.getInstance().addIdleListener(
            this,
            TimeUnit.MILLISECONDS.convert(
                getCurrentTimoutInMinutes(),
                TimeUnit.MINUTES
            ).toInt()
        )
    }

    private fun getCurrentTimoutInMinutes() =
        WaifuMotivatorPluginState.getInstance().state?.idleTimeoutInMinutes ?: DEFAULT_IDLE_TIMEOUT_IN_MINUTES

    override fun dispose() {
        messageBus.dispose()
        IdeEventQueue.getInstance().removeIdleListener(this)
    }

    override fun run() {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(MotivationEventListener.TOPIC)
            .onEventTrigger(
                MotivationEvent(
                    MotivationEvents.IDLE,
                    MotivationEventCategory.NEUTRAL,
                    "Idle Events",
                    project
                ) { createAlertConfiguration() }
            )
    }

    private fun createAlertConfiguration(): AlertConfiguration {
        val pluginState = WaifuMotivatorPluginState.getInstance().state
        return AlertConfiguration(
            pluginState!!.isIdleMotivationEnabled || pluginState.isIdleSoundEnabled,
            pluginState.isIdleMotivationEnabled,
            pluginState.isIdleSoundEnabled
        )
    }
}
