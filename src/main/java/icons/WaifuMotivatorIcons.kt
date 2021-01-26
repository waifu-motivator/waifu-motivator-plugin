package icons

import com.intellij.openapi.util.IconLoader

object WaifuMotivatorIcons {
    @JvmField
    val MENU = IconLoader.getIcon("/icons/wmp.svg", javaClass)

    @JvmField
    val CALENDAR = IconLoader.getIcon("/icons/emojis/1f5d3.svg", javaClass)

    object Plugins {
        object AMII {
            @JvmField
            val TOOL_WINDOW = IconLoader.getIcon("/icons/plugins/amii/plugin-tool-window.svg", javaClass)
        }
    }
}
