package zd.zero.waifu.motivator.plugin.motivation

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.alert.NonTitledVisualNotification
import zd.zero.waifu.motivator.plugin.alert.VisualWaifuNotification
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory

object VisualMotivationFactory : WaifuMotivationFactory {
    override fun constructMotivation(
        project: Project,
        motivation: MotivationAsset,
        config: AlertConfiguration
    ): WaifuMotivation =
        VisualMotivation(
            VisualWaifuNotification(motivation, project),
            WaifuSoundPlayerFactory.createPlayer(motivation.soundFilePath),
            config
        )

    fun constructNonTitledMotivation(
        project: Project,
        motivation: MotivationAsset,
        config: AlertConfiguration
    ): WaifuMotivation =
        VisualMotivation(
            NonTitledVisualNotification(motivation, project),
            WaifuSoundPlayerFactory.createPlayer(motivation.soundFilePath),
            config
        )
}
