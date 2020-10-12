package zd.zero.waifu.motivator.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.MotivationFactory.showUntitledMotivationEventFromCategories
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import java.util.*

class MotivateMeAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val pluginState = WaifuMotivatorPluginState.getPluginState()
        val config = AlertConfiguration(
            pluginState.isMotivateMeEnabled || pluginState.isMotivateMeSoundEnabled,
            pluginState.isMotivateMeEnabled,
            pluginState.isMotivateMeSoundEnabled)
        showUntitledMotivationEventFromCategories(
            MotivationEvent(
                MotivationEvents.MISC,
                MotivationEventCategory.POSITIVE,
                "Motivate Me",
                Objects.requireNonNull(e.project)!!
            ) { config },
            WaifuAssetCategory.CELEBRATION,
            WaifuAssetCategory.HAPPY,
            WaifuAssetCategory.SMUG
        )
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
