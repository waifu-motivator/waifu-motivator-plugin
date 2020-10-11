package zd.zero.waifu.motivator.plugin.motivation

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.alert.VisualWaifuNotification
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.alert.NonTitledVisualNotification
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory
import zd.zero.waifu.motivator.plugin.tools.doOrElse

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


object MotivationFactory {

    fun showMotivationEventForCategory(
        motivationEvent: MotivationEvent,
        waifuAssetCategory: WaifuAssetCategory
    ) {
        val project = motivationEvent.project
        VisualMotivationAssetProvider.createAssetByCategory(waifuAssetCategory)
            .doOrElse({ asset ->
                val motivation = VisualMotivationFactory.constructMotivation(
                    project,
                    asset,
                    motivationEvent.alertConfigurationSupplier()
                ).setListener(
                    object : MotivationListener {
                        override fun onDisposal() {

                        }
                    }
                )
                if (project.isInitialized) {
                    motivation.motivate()
                } else {
                    StartupManager.getInstance(project)
                        .registerPostStartupActivity {
                            motivation.motivate()
                        }
                }
            }) {
                UpdateNotification.sendMessage(
                    "'${motivationEvent.title}' unavailable offline.",
                    ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                    project
                )
            }
    }
}
