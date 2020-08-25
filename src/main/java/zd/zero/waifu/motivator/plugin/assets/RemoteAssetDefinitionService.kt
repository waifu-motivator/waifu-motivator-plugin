package zd.zero.waifu.motivator.plugin.assets

import kotlin.random.Random

abstract class RemoteAssetDefinitionService<T : AssetDefinition>(
    private val remoteAssetManager: RemoteAssetManager<T>
) {
    private val random = Random(System.currentTimeMillis())

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): T {
        return remoteAssetManager.resolveAsset(
            pickRandomAsset(remoteAssetManager.supplyAssetDefinitions(), waifuAssetCategory)
        ).orElseGet {
            pickRandomAsset(remoteAssetManager.supplyLocalAssetDefinitions(), waifuAssetCategory)
        }
    }

    // todo: handle empty list better.
    private fun pickRandomAsset(supplyLocalAssetDefinitions: List<T>, waifuAssetCategory: WaifuAssetCategory): T {
        return supplyLocalAssetDefinitions
            .filter { it.categories.contains(waifuAssetCategory) }
            .random(random)
    }
}
