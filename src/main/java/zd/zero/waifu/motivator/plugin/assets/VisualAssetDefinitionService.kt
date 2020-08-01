package zd.zero.waifu.motivator.plugin.assets

object VisualAssetDefinitionService {

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory) =
        VisualAssetManager.resolveAsset(
            VisualAssetManager.supplyAssetDefinitions()
            .filter { it.categories.contains(waifuAssetCategory) }
            .random()
        )
}
