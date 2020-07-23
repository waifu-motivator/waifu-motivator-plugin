package zd.zero.waifu.motivator.plugin.alert

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration

interface WaifuMotivationFactory {
    fun constructMotivation(project: Project, motivation: MotivationAsset, config: AlertConfiguration): WaifuMotivation
}
