package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification


class PluginInstallListener : DynamicPluginListener {

    companion object {
        var isRunningUpdate = false
    }

    override fun beforePluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
    }

    override fun checkUnloadPlugin(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == WaifuMotivator.PLUGIN_ID) {
            isRunningUpdate = true
            ProjectManager.getInstance().openProjects.forEach {
                ProjectManager.getInstance().reloadProject(it)
            }
            isRunningUpdate = false
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {}
}
