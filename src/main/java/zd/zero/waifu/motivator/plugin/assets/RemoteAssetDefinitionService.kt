package zd.zero.waifu.motivator.plugin.assets

import kotlin.random.Random

abstract class RemoteAssetDefinitionService<T : AssetDefinition>(
    protected val remoteAssetManager: RemoteAssetManager<T>
) {
    protected val random = Random(System.currentTimeMillis())

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): T =
        remoteAssetManager.resolveAsset(
            remoteAssetManager.supplyAssetDefinitions()
                .filter { it.categories.contains(waifuAssetCategory) }
                .random(random)
        )
}
