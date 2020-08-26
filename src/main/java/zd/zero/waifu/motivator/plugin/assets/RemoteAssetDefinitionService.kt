package zd.zero.waifu.motivator.plugin.assets

import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.*
import kotlin.random.Random

abstract class RemoteAssetDefinitionService<T : AssetDefinition, U : Asset>(
    private val remoteAssetManager: RemoteAssetManager<T, U>
) {
    private val random = Random(System.currentTimeMillis())

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): Optional<U> =
        remoteAssetManager.resolveAsset(
            pickRandomAsset(remoteAssetManager.supplyAssetDefinitions(), waifuAssetCategory)
        ).map { it.toOptional() } // todo: replace with or when supporting only JRE 11+
            .orElseGet {
                remoteAssetManager.resolveAsset(
                    pickRandomAsset(remoteAssetManager.supplyLocalAssetDefinitions(), waifuAssetCategory)
                )
            }

    // todo: handle empty list better.
    private fun pickRandomAsset(supplyLocalAssetDefinitions: List<T>, waifuAssetCategory: WaifuAssetCategory): T {
        return supplyLocalAssetDefinitions
            .filter { it.categories.contains(waifuAssetCategory) }
            .random(random)
    }
}
