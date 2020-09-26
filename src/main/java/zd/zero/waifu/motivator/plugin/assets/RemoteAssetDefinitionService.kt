package zd.zero.waifu.motivator.plugin.assets

import com.google.common.annotations.VisibleForTesting
import com.intellij.openapi.application.ApplicationManager
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.*
import kotlin.random.Random

abstract class RemoteAssetDefinitionService<T : AssetDefinition, U : Asset>(
    private val remoteAssetManager: RemoteAssetManager<T, U>
) {
    private val random = Random(System.currentTimeMillis())

    fun getRandomAssetByCategory(
        waifuAssetCategory: WaifuAssetCategory
    ): Optional<U> =
        pickRandomAsset(remoteAssetManager.supplyLocalAssetDefinitions(), waifuAssetCategory)
            .map {
                downloadNewAsset(waifuAssetCategory)
                remoteAssetManager.resolveAsset(it)
            }.orElseGet {
                fetchRemoteAsset(waifuAssetCategory)
            }

    @VisibleForTesting
    protected open fun downloadNewAsset(waifuAssetCategory: WaifuAssetCategory) {
        ApplicationManager.getApplication().executeOnPooledThread { fetchRemoteAsset(waifuAssetCategory) }
    }

    private fun fetchRemoteAsset(
        waifuAssetCategory: WaifuAssetCategory
    ): Optional<U> =
        pickRandomAsset(remoteAssetManager.supplyRemoteAssetDefinitions(), waifuAssetCategory)
            .flatMap { remoteAssetManager.resolveAsset(it) }

    private fun pickRandomAsset(
        assetDefinitions: Collection<T>,
        waifuAssetCategory: WaifuAssetCategory
    ): Optional<T> =
        assetDefinitions
            .filter { it.categories.contains(waifuAssetCategory) }
            .toOptional()
            .filter { it.isNotEmpty() }
            .map { it.random(random) }
}
