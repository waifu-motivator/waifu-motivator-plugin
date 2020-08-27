package zd.zero.waifu.motivator.plugin.tools

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory

object AssetTools {

    private const val MAXIMUM_RETRY_ATTEMPTS = 6

    fun attemptToShowCategories(
        project: Project,
        alertConfigurationSupplier: () -> AlertConfiguration,
        onFailure: () -> Unit,
        vararg categories: WaifuAssetCategory
    ) {
        attemptToDisplayNotification(
                project,
                alertConfigurationSupplier,
                onFailure,
                0,
                *categories
        )
    }

    private fun attemptToDisplayNotification(
        project: Project,
        alertConfigurationSupplier: () -> AlertConfiguration,
        onFailure: () -> Unit,
        attempts: Int,
        vararg categories: WaifuAssetCategory
    ) {
        if (attempts < MAXIMUM_RETRY_ATTEMPTS) {
            VisualMotivationAssetProvider.pickAssetFromCategories(
                    *categories
            ).doOrElse({ asset ->
                VisualMotivationFactory.constructMotivation(project,
                        asset,
                        alertConfigurationSupplier()).motivate()
            }) {
                attemptToDisplayNotification(
                        project,
                        alertConfigurationSupplier,
                        onFailure,
                        attempts + 1,
                        *categories
                )
            }
        } else {
            onFailure()
        }
    }
}
