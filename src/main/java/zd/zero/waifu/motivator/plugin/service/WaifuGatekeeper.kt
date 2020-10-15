package zd.zero.waifu.motivator.plugin.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import zd.zero.waifu.motivator.plugin.settings.PluginSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState

class WaifuGatekeeper : Disposable {
    companion object {
        val instance: WaifuGatekeeper
            get() = ServiceManager.getService(WaifuGatekeeper::class.java)
    }

    private val connection = ApplicationManager.getApplication().messageBus.connect()

    private var allowedWaifu: Set<String> = extractAllowedCharactersFromState(WaifuMotivatorPluginState.getPluginState())

    private fun extractAllowedCharactersFromState(pluginState: WaifuMotivatorState): Set<String> =
        pluginState.preferredCharacters.split(WaifuMotivatorState.DEFAULT_DELIMITER)
            .filter { it.isNotEmpty() }
            .map { it.toLowerCase() }
            .toSet()

    init {
        connection.subscribe(PluginSettingsListener.PLUGIN_SETTINGS_TOPIC, object : PluginSettingsListener {
            override fun settingsUpdated(newPluginState: WaifuMotivatorState) {
                allowedWaifu = extractAllowedCharactersFromState(newPluginState)
            }
        })
    }

    fun isAllowed(characters: List<String>?): Boolean =
        allowedWaifu.isEmpty() ||
            characters?.any { allowedWaifu.contains(it.toLowerCase()) } ?: false

    fun isPreferred(character: String): Boolean =
        allowedWaifu.contains(character.toLowerCase())

    override fun dispose() {
        connection.dispose()
    }
}
