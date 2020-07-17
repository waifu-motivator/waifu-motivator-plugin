package zd.zero.waifu.motivator.plugin.onboarding

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.*

object UserOnboarding {

    fun attemptToShowUpdateNotification() {
        getVersion()
            .filter { it != WaifuMotivatorPluginState.getPluginState().version }
            .ifPresent { newVersion ->
                WaifuMotivatorPluginState.getPluginState().version = newVersion
                UpdateNotification.display(ProjectManager.getInstance().defaultProject, newVersion)
            }
    }

    private fun getVersion(): Optional<String> =
        PluginManagerCore.getPlugin(PluginId.getId(WaifuMotivator.PLUGIN_ID))
            .toOptional()
            .map { it.version }
}