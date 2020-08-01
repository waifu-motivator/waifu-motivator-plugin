package zd.zero.waifu.motivator.plugin.assets

object VisualAssetDefinitionService {

    fun getRandomCelebrationVisualAsset(): VisualMotivationAssetDefinition {
        val celebrationAsset = VisualAssetManager.supplyAssetDefinitions()
            .filter {
                it.categories.contains(WaifuAssetCategory.CELEBRATION)
            }.random()
        return VisualAssetManager.resolveAsset(celebrationAsset)
    }
}
