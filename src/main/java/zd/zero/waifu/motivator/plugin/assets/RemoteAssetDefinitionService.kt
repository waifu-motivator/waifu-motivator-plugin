package zd.zero.waifu.motivator.plugin.assets

import com.google.common.annotations.VisibleForTesting
import com.intellij.openapi.application.ApplicationManager
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.Optional
import java.util.UUID
import kotlin.random.Random

abstract class RemoteAssetDefinitionService<T : AssetDefinition, U : Asset>(
    private val remoteAssetManager: RemoteAssetManager<T, U>
) {
    private val random = Random(System.currentTimeMillis())

    fun getAssetByGroupId(
        groupId: UUID,
        category: WaifuAssetCategory
    ): Optional<U> =
        remoteAssetManager.supplyLocalAssetDefinitions()
            .find {
                it.groupId == groupId
            }.toOptional()
            .map {
                resolveAsset(category, it)
            }.orElseGet {
                remoteAssetManager.supplyRemoteAssetDefinitions()
                    .find { it.groupId == groupId }
                    .toOptional()
                    .map { remoteAssetManager.resolveAsset(it) }
                    .orElseGet {
                        getRandomAssetByCategory(category)
                    }
            }

    fun getRandomAssetByCategory(
        waifuAssetCategory: WaifuAssetCategory
    ): Optional<U> =
        pickRandomAsset(waifuAssetCategory)

    fun getRandomUngroupedAssetByCategory(
        waifuAssetCategory: WaifuAssetCategory
    ): Optional<U> =
        pickRandomAsset(waifuAssetCategory) { it.groupId == null }

    private fun pickRandomAsset(
        waifuAssetCategory: WaifuAssetCategory,
        assetPredicate: (T) -> Boolean = { true }
    ) =
        pickRandomAsset(
            remoteAssetManager.supplyLocalAssetDefinitions()
                .filterByCategory(waifuAssetCategory)
                .filter(assetPredicate)
                .ifEmpty {
                    remoteAssetManager.supplyAllLocalAssetDefinitions()
                        .filterByCategory(waifuAssetCategory)
                }
        )
            .map {
                resolveAsset(waifuAssetCategory, it)
            }.orElseGet {
                fetchRemoteAsset(waifuAssetCategory, assetPredicate)
            }

    private fun resolveAsset(
        waifuAssetCategory: WaifuAssetCategory,
        it: T
    ): Optional<U> {
        downloadNewAsset(waifuAssetCategory)
        return remoteAssetManager.resolveAsset(it)
    }

    @VisibleForTesting
    protected open fun downloadNewAsset(waifuAssetCategory: WaifuAssetCategory) {
        ApplicationManager.getApplication()
            .executeOnPooledThread { fetchRemoteAsset(waifuAssetCategory) }
    }

    private fun fetchRemoteAsset(
        waifuAssetCategory: WaifuAssetCategory,
        assetPredicate: (T) -> Boolean = { true }
    ): Optional<U> =
        pickRandomAsset(
            remoteAssetManager.supplyRemoteAssetDefinitions()
                .filterByCategory(waifuAssetCategory)
                .filter(assetPredicate)
                .ifEmpty {
                    remoteAssetManager.supplyAllRemoteAssetDefinitions()
                        .filterByCategory(waifuAssetCategory)
                }
        ).flatMap { remoteAssetManager.resolveAsset(it) }

    private fun pickRandomAsset(
        assetDefinitions: Collection<T>
    ): Optional<T> =
        assetDefinitions
            .toOptional()
            .filter { it.isNotEmpty() }
            .map { it.random(random) }
}

fun <T : AssetDefinition> Collection<T>.filterByCategory(category: WaifuAssetCategory): Collection<T> =
    this.filter { it.categories.contains(category) }
