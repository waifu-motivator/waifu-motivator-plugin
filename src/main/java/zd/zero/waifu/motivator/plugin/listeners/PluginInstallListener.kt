package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification

class MotivatorProject(private val project: Project) : ProjectManagerListener {

    init {
        UpdateNotification.sendMessage(
            "Motivator Project",
            "Instantiated",
            project
        )

    }

    override fun projectOpened(project: Project) {
        UpdateNotification.sendMessage(
            "Motivator Project",
            "Project Opened",
            this.project
        )

    }

    override fun projectClosed(project: Project) {
        UpdateNotification.sendMessage(
            "Motivator Project",
            "Project Closed",
            this.project
        )
    }
}


class PluginInstallListener : DynamicPluginListener {

    override fun beforePluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
    }

    override fun checkUnloadPlugin(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == WaifuMotivator.PLUGIN_ID) {
            ProjectManager.getInstance().openProjects.forEach {
                ProjectManager.getInstance().reloadProject(it)
//                UpdateNotification.sendMessage(
//                    "Ravioli Ravioli",
//                    "Give me the formuoli",
//                    it
//                )

            }
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {}
}
