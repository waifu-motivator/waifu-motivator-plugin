package zd.zero.waifu.motivator.plugin.onboarding

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.platform.UpdateListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.*

object UserOnboarding {

    fun attemptToPerformNewUpdateActions() {
        getNewVersion().ifPresent { newVersion ->
            WaifuMotivatorPluginState.getPluginState().version = newVersion
            ApplicationManager.getApplication().messageBus
                .syncPublisher(UpdateListener.TOPIC)
                .onUpdate()
            UpdateNotification.display(ProjectManager.getInstance().defaultProject, newVersion)
        }

        if (WaifuMotivatorPluginState.getPluginState().userId.isEmpty()) {
            WaifuMotivatorPluginState.getPluginState().userId = UUID.randomUUID().toString()
        }
    }

    private fun getNewVersion() =
        getVersion()
            .filter { it != WaifuMotivatorPluginState.getPluginState().version }

    fun isNewVersion() =
        getNewVersion().isPresent

    private fun getVersion(): Optional<String> =
        PluginManagerCore.getPlugin(PluginId.getId(WaifuMotivator.PLUGIN_ID))
            .toOptional()
            .map { it.version }
}
