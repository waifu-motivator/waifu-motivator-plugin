package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification

class PluginInstallListener : DynamicPluginListener {
    override fun beforePluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
    }

    override fun checkUnloadPlugin(pluginDescriptor: IdeaPluginDescriptor) {
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == WaifuMotivator.PLUGIN_ID) {
            UpdateNotification.sendMessage(
                "Ravioli Ravioli",
                "Give me the formuoli"
            )
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {

    }
}
