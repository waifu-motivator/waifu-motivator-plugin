package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState.Companion.DEFAULT_IDLE_TIMEOUT

class IdleEventListener : Runnable, Disposable {

    init {
        // todo: listen for config updates
        IdeEventQueue.getInstance().addIdleListener(
            this,
            WaifuMotivatorPluginState.getInstance().state?.idleTimout?.toInt() ?: DEFAULT_IDLE_TIMEOUT.toInt()
        )
    }

    override fun dispose() {
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
            pluginState!!.isUnitTesterMotivationEnabled || pluginState.isUnitTesterMotivationSoundEnabled,
            pluginState.isUnitTesterMotivationEnabled,
            pluginState.isUnitTesterMotivationSoundEnabled
        )
    }
}
