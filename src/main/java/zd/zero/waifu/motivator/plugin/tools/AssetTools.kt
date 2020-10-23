package zd.zero.waifu.motivator.plugin.tools

import zd.zero.waifu.motivator.plugin.assets.MotivationAsset
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import java.util.Optional

object AssetTools {

    private const val MAXIMUM_RETRY_ATTEMPTS = 6

    fun resolveAssetFromCategories(
        vararg categories: WaifuAssetCategory
    ): Optional<MotivationAsset> {
        return attemptToGetMotivationAssetFromCategories(
            0,
            *categories
        )
    }

    private fun attemptToGetMotivationAssetFromCategories(
        attempts: Int,
        vararg categories: WaifuAssetCategory
    ): Optional<MotivationAsset> {
        return if (attempts < MAXIMUM_RETRY_ATTEMPTS) {
            VisualMotivationAssetProvider.pickAssetFromCategories(
                *categories
            ).map { it.toOptional() }
                .orElseGet {
                    attemptToGetMotivationAssetFromCategories(
                        attempts + 1,
                        *categories
                    )
                }
        } else {
            Optional.empty()
        }
    }
}
