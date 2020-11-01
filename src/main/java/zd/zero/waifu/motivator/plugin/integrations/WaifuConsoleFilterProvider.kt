package zd.zero.waifu.motivator.plugin.integrations

import com.intellij.execution.filters.ConsoleFilterProvider
import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.MotivationFactory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState.getPluginState

class WaifuConsoleFilterProvider : ConsoleFilterProvider {

    private var prevLength: Int = Int.MAX_VALUE

    private var alertShown = false

    override fun getDefaultFilters(project: Project): Array<Filter> = when {
        getPluginState().isLogWatcherEnabled -> arrayOf(
            Filter { line, entireLength ->
                val newConsole = entireLength < prevLength
                if (newConsole) alertShown = false

                val pluginState = getPluginState()
                if (alertShown.not() && pluginState.logWatcherKeyword.isNotEmpty() &&
                    line.contains(pluginState.logWatcherKeyword, ignoreCase = pluginState.isLogWatcherCaseSensitivityIgnored)
                ) {
                    MotivationFactory.showUntitledMotivationEventFromCategories(
                        MotivationEvent(
                            MotivationEvents.MISC,
                            MotivationEventCategory.POSITIVE,
                            "Waifu Log Watcher Event",
                            project
                        ) { AlertConfiguration.allEnabled() },
                        WaifuAssetCategory.ACKNOWLEDGEMENT,
                        WaifuAssetCategory.CELEBRATION,
                        WaifuAssetCategory.HAPPY,
                        WaifuAssetCategory.SMUG
                    )

                    alertShown = true
                }

                prevLength = entireLength
                return@Filter null
            }
        )
        else -> Filter.EMPTY_ARRAY
    }
}
