package zd.zero.waifu.motivator.plugin.motivation.event

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration

data class MotivationEvent(
    val type: MotivationEvents,
    val category: MotivationEventCategory,
    val title: String,
    val project: Project,
    val alertConfigurationSupplier: () -> AlertConfiguration
)
