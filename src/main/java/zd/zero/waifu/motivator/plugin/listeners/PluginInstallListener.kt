package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.startup.StartupManager
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.WaifuOfTheDayStartupActivity
import zd.zero.waifu.motivator.plugin.onboarding.UserOnboarding

class PluginInstallListener : DynamicPluginListener {

    override fun beforePluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
    }

    override fun checkUnloadPlugin(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == WaifuMotivator.PLUGIN_ID) {
            val project = ProjectManager.getInstance().openProjects.first() ?: return
            StartupManager.getInstance(project)
                .runWhenProjectIsInitialized {
                    UserOnboarding.attemptToPerformNewUpdateActions()
                    WaifuOfTheDayStartupActivity().runActivity(project)
                }
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {}
}
