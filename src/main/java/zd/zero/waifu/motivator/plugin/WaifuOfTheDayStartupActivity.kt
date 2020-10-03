package zd.zero.waifu.motivator.plugin

import com.intellij.ide.GeneralSettings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.util.Alarm
import zd.zero.waifu.motivator.plugin.alert.dialog.WaifuOfTheDayDialog
import zd.zero.waifu.motivator.plugin.personality.Wendi
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

class WaifuOfTheDayStartupActivity : StartupActivity.DumbAware {

    companion object {
        private const val IS_INITIAL_TIP_OF_THE_DAY_UPDATED = "WMP_IS_INITIAL_TIP_OF_THE_DAY_UPDATED"
    }

    override fun runActivity(project: Project) {
        Wendi.initialize()
        updatePlatformTipOfTheDayConfig()

        Alarm(project).addRequest(
            { WaifuOfTheDayDialog.canBeShownToday(project) },
            0, ModalityState.any()
        )
    }

    private fun updatePlatformTipOfTheDayConfig() {
        val isInitialPlatformTipUpdated = PropertiesComponent.getInstance()
            .getValue(IS_INITIAL_TIP_OF_THE_DAY_UPDATED, "")
            .toBoolean()
        if (isInitialPlatformTipUpdated) return;

        PropertiesComponent.getInstance().setValue(IS_INITIAL_TIP_OF_THE_DAY_UPDATED, true)
        GeneralSettings.getInstance().isShowTipsOnStartup =
            !WaifuMotivatorPluginState.getPluginState().isWaifuOfTheDayEnabled
    }

}
