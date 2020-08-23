package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.GeneralSettings
import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

class IdleEventListener : Runnable, Disposable {

    init {
        val inactiveTimeout = GeneralSettings.getInstance().inactiveTimeout
        IdeEventQueue.getInstance().addIdleListener(this,  10000)
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
