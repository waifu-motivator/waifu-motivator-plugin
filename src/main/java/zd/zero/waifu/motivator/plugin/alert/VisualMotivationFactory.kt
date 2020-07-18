package zd.zero.waifu.motivator.plugin.alert

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration


object VisualMotivationFactory : WaifuMotivationFactory {
    override fun constructMotivation(
        project: Project,
        motivationAsset: MotivationAsset,
        config: AlertConfiguration
    ): WaifuMotivation {
        TODO("Not yet implemented")
    }
}
