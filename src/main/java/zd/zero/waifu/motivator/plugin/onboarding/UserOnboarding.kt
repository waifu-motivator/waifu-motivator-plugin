package zd.zero.waifu.motivator.plugin.onboarding

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.promotion.PromotionManager
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.Optional
import java.util.UUID

object UserOnboarding {
    fun attemptToPerformNewUpdateActions() {
        getNewVersion().ifPresent { newVersion ->
            WaifuMotivatorPluginState.getPluginState().version = newVersion
            UpdateNotification.display(ProjectManager.getInstance().defaultProject, newVersion)
        }

        val isNewUser = WaifuMotivatorPluginState.getPluginState().userId.isEmpty()
        getVersion().ifPresent { version ->
            PromotionManager.registerPromotion(version, isNewUser = isNewUser)
        }
        if (isNewUser) {
            WaifuMotivatorPluginState.getPluginState().userId = UUID.randomUUID().toString()
        }
    }

    private fun getNewVersion() =
        getVersion()
            .filter { it != WaifuMotivatorPluginState.getPluginState().version }

    private fun getVersion(): Optional<String> =
        PluginManagerCore.getPlugin(PluginId.getId(WaifuMotivator.PLUGIN_ID))
            .toOptional()
            .map { it.version }
}
