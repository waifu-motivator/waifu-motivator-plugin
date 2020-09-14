package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.project.ProjectManager
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification

class PluginInstallListener : DynamicPluginListener {

    init {
        ProjectManager.getInstance().openProjects.forEach {
            UpdateNotification.sendMessage(
                "threw it on the ground",
                "What you think I'm stupid?"
            )
        }
    }

    override fun beforePluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
    }

    override fun checkUnloadPlugin(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == WaifuMotivator.PLUGIN_ID) {
            ProjectManager.getInstance().openProjects.forEach {
                UpdateNotification.sendMessage(
                    "Ravioli Ravioli",
                    "Give me the formuoli",
                    it
                )

            }
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        if (pluginDescriptor.pluginId.idString == WaifuMotivator.PLUGIN_ID) {
        }
    }
}
