package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.settings.ThemeSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState.Companion.DEFAULT_IDLE_TIMEOUT_IN_MINUTES
import java.util.concurrent.TimeUnit

class IdleEventListener : Runnable, Disposable {
    private val messageBus = ApplicationManager.getApplication().messageBus.connect()

    init {
        val self = this
        messageBus.subscribe(ThemeSettingsListener.THEME_SETTINGS_TOPIC, object : ThemeSettingsListener {
            override fun themeSettingsUpdated(themeSettings: WaifuMotivatorState) {
                IdeEventQueue.getInstance().removeIdleListener(self)
                IdeEventQueue.getInstance().addIdleListener(self,
                    TimeUnit.MILLISECONDS.convert(
                        themeSettings.idleTimoutInMinutes,
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
        WaifuMotivatorPluginState.getInstance().state?.idleTimoutInMinutes ?: DEFAULT_IDLE_TIMEOUT_IN_MINUTES

    override fun dispose() {
        messageBus.dispose()
        IdeEventQueue.getInstance().removeIdleListener(this)
    }

    override fun run() {
        VisualMotivationFactory.constructMotivation(
            ProjectManager.getInstance().defaultProject,
            VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.WAITING),
            createAlertConfiguration()
        ).motivate()
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
