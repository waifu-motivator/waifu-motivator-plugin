package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification.sendMessage
import zd.zero.waifu.motivator.plugin.settings.PluginSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState.Companion.DEFAULT_IDLE_TIMEOUT_IN_MINUTES
import zd.zero.waifu.motivator.plugin.tools.doOrElse
import java.util.concurrent.TimeUnit

class IdleEventListener : Runnable, Disposable {
    private val messageBus = ApplicationManager.getApplication().messageBus.connect()

    init {
        val self = this
        messageBus.subscribe(PluginSettingsListener.PLUGIN_SETTINGS_TOPIC, object : PluginSettingsListener {
            override fun settingsUpdated(newPluginState: WaifuMotivatorState) {
                IdeEventQueue.getInstance().removeIdleListener(self)
                IdeEventQueue.getInstance().addIdleListener(self,
                    TimeUnit.MILLISECONDS.convert(
                        newPluginState.idleTimeoutInMinutes,
                        TimeUnit.MINUTES
                    ).toInt()
                )
            }
        })
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

    private var isEventDisplayed = false
    override fun run() {
        if (isEventDisplayed.not()) {
            val project = ProjectManager.getInstance().defaultProject
            VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.WAITING)
                .doOrElse({ asset ->
                    isEventDisplayed = true
                    VisualMotivationFactory.constructMotivation(
                        project,
                        asset,
                        createAlertConfiguration()
                    ).setListener {
                        isEventDisplayed = false
                    }.motivate()
                }) {
                    sendMessage(
                        "'Idle Events' Unavailable Offline",
                        ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                        project
                    )
                }
        }
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
