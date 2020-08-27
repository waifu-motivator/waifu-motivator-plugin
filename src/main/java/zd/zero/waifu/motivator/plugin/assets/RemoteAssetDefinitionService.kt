package zd.zero.waifu.motivator.plugin.assets

import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.*
import kotlin.random.Random

abstract class RemoteAssetDefinitionService<T : AssetDefinition, U : Asset>(
    private val remoteAssetManager: RemoteAssetManager<T, U>
) {
    private val random = Random(System.currentTimeMillis())

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): Optional<U> =
        pickRandomAsset(remoteAssetManager.supplyAssetDefinitions(), waifuAssetCategory)
            .flatMap { randomRemoteAsset ->
                remoteAssetManager.resolveAsset(randomRemoteAsset)
            }
            .map { it.toOptional() } // todo: replace with or when supporting only JRE 11+
            .orElseGet {
                pickRandomAsset(remoteAssetManager.supplyLocalAssetDefinitions(), waifuAssetCategory)
                    .flatMap { randomLocalAsset ->
                        remoteAssetManager.resolveAsset(randomLocalAsset)
                    }
            }

    private fun pickRandomAsset(
        supplyLocalAssetDefinitions: List<T>,
        waifuAssetCategory: WaifuAssetCategory
    ): Optional<T> =
        supplyLocalAssetDefinitions
            .filter { it.categories.contains(waifuAssetCategory) }
            .toOptional()
            .filter { it.isNotEmpty() }
            .map { it.random(random) }
}
