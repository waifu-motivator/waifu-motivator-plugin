package zd.zero.waifu.motivator.plugin.settings

import com.intellij.util.messages.Topic
import java.util.*

interface ThemeSettingsListener : EventListener {
    companion object {
        @JvmStatic
        val THEME_SETTINGS_TOPIC: Topic<ThemeSettingsListener> =
            Topic(ThemeSettingsListener::class.java)
    }

    fun themeSettingsUpdated(themeSettings: WaifuMotivatorState)
}
