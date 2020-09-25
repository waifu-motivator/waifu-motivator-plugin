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
        attemptToDisplayMotivation(
            project,
            alertConfigurationSupplier,
            onFailure,
            0,
            *categories
        )
    }

    private fun attemptToDisplayMotivation(
        project: Project,
        alertConfigurationSupplier: () -> AlertConfiguration,
        onFailure: () -> Unit,
        attempts: Int,
        vararg categories: WaifuAssetCategory
    ) {
        if (attempts < MAXIMUM_RETRY_ATTEMPTS) {
            VisualMotivationAssetProvider.pickAssetFromCategories(
                *categories
            ).ifPresentOrElse({ asset ->
                VisualMotivationFactory.constructMotivation(project,
                    asset,
                    alertConfigurationSupplier()).motivate()
            }) {
                attemptToDisplayMotivation(
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
