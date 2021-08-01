package zd.zero.waifu.motivator.plugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

@State(
    name = "WaifuMotivatorPluginSettings",
    storages = [Storage("waifu-motivator-plugin.xml")]
)
class WaifuMotivatorPluginState : PersistentStateComponent<WaifuMotivatorState?> {

    private val state = WaifuMotivatorState()

    companion object {
        @JvmStatic
        val instance: WaifuMotivatorPluginState
            get() = ApplicationManager.getApplication().getService(WaifuMotivatorPluginState::class.java)

        @JvmStatic
        val pluginState: WaifuMotivatorState
            get() = instance.getState()
    }

    override fun getState(): WaifuMotivatorState {
        return state
    }

    override fun loadState(state: WaifuMotivatorState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

}
