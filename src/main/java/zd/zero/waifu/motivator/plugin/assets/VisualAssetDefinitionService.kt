package zd.zero.waifu.motivator.plugin.assets

import kotlin.random.Random

object VisualAssetDefinitionService {
    private val random = Random(System.currentTimeMillis())

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory) =
        VisualAssetManager.resolveAsset(
            VisualAssetManager.supplyAssetDefinitions()
            .filter { it.categories.contains(waifuAssetCategory) }
            .random(random)
        )
}
