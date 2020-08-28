package zd.zero.waifu.motivator.plugin.assets

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test
import java.util.*

internal class FakeVisualAssetDefinitionService(
    assetManager: RemoteAssetManager<VisualMotivationAssetDefinition, VisualMotivationAsset>
) : RemoteAssetDefinitionService<VisualMotivationAssetDefinition, VisualMotivationAsset>(assetManager)

class RemoteAssetDefinitionServiceTest {

    @MockK
    private lateinit var remoteAssetManager: RemoteAssetManager<VisualMotivationAssetDefinition, VisualMotivationAsset>

    @InjectMockKs
    private lateinit var fakeVisualAssetDefinitionService: FakeVisualAssetDefinitionService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getRandomAssetByCategoryShouldReturnEmptyWhenNoAssets() {
        every { remoteAssetManager.supplyAssetDefinitions() } returns listOf()
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.isEmpty).isTrue()
    }

    @Test
    fun getRandomAssetByCategoryShouldReturnEmptyWhenUnableToFetchRemoteAssetWithNoLocalAssets() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyAssetDefinitions() } returns listOf(bestMotivation)
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns Optional.empty()
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.isEmpty).isTrue()
    }

    @Test
    fun getRandomAssetByCategoryShouldReturnEmptyWhenUnableToFetchRemoteAssetWithNoMatchingLocalAssets() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyAssetDefinitions() } returns listOf(bestMotivation)
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns Optional.empty()

        val garbageWaifu = VisualMotivationAssetDefinition(
            "Trash Chan",
            "Trash Chan",
            "Literal Garbage",
            ImageDimension(42, 9001),
            listOf(WaifuAssetCategory.SHOCKED)
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(garbageWaifu)

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.isEmpty).isTrue()
    }
}
