package zd.zero.waifu.motivator.plugin.alert

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory


object VisualMotivationFactory : WaifuMotivationFactory {
    override fun constructMotivation(
        project: Project,
        motivationAsset: MotivationAsset,
        config: AlertConfiguration
    ): WaifuMotivation =
        VisualMotivation(
            WaifuSoundPlayerFactory.createPlayer(motivationAsset.soundFileName),
            config
        )
}
