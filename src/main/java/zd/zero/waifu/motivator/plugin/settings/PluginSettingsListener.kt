package zd.zero.waifu.motivator.plugin.settings

import com.intellij.util.messages.Topic
import java.util.*

fun interface PluginSettingsListener : EventListener {
    companion object {
        @JvmStatic
        val PLUGIN_SETTINGS_TOPIC: Topic<PluginSettingsListener> =
            Topic(PluginSettingsListener::class.java)
    }

    fun settingsUpdated(newPluginState: WaifuMotivatorState)
}
