package zd.zero.waifu.motivator.plugin.service

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId

const val AMII_PLUGIN_ID = "io.unthrottled.amii"

object PluginService {
    fun isAmiiInstalled(): Boolean =
        PluginManagerCore.isPluginInstalled(
            PluginId.getId(AMII_PLUGIN_ID),
        )
}
