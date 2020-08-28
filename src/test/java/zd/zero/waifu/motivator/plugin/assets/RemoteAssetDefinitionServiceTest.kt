package zd.zero.waifu.motivator.plugin.assets

import com.jetbrains.rd.framework.base.deepClonePolymorphic
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import zd.zero.waifu.motivator.plugin.tools.toOptional
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
    fun getRandomAssetByCategoryShouldReturnLocalWhenNoRemoteAssets() {
        every { remoteAssetManager.supplyAssetDefinitions() } returns listOf()

        val bestLocalMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(bestLocalMotivation)
        val expectedAsset = bestLocalMotivation.toAsset("file:///s-tier-waifus/ryuko.png")
        every { remoteAssetManager.resolveAsset(bestLocalMotivation) } returns expectedAsset.deepClonePolymorphic()
            .toOptional()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.get()).isEqualTo(expectedAsset)
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
    fun getRandomAssetByCategoryShouldReturnRemoteWhenAvailable() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyAssetDefinitions() } returns listOf(bestMotivation)

        val expectedAsset = bestMotivation.toAsset("file:///s-tier-waifus/ryuko.png")
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns expectedAsset.deepClonePolymorphic()
            .toOptional()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.get()).isEqualTo(expectedAsset)
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


    @Test
    fun getRandomAssetByCategoryShouldReturnEmptyWhenLocalAssetIsNotLocal() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyAssetDefinitions() } returns listOf(bestMotivation)
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns Optional.empty()

        val bestLocalMotivation = VisualMotivationAssetDefinition(
            "Local Ryuko",
            "Local Ryuko",
            "Global Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(bestLocalMotivation)
        val expectedAsset = bestLocalMotivation.toAsset("file:///s-tier-waifus/ryuko.png")
        every { remoteAssetManager.resolveAsset(bestLocalMotivation) } returns Optional.empty()


        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.isEmpty).isTrue()
    }

    @Test
    fun getRandomAssetByCategoryShouldReturnLocalAssetRemoteAssetIsNotAvailable() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyAssetDefinitions() } returns listOf(bestMotivation)
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns Optional.empty()

        val bestLocalMotivation = VisualMotivationAssetDefinition(
            "Local Ryuko",
            "Local Ryuko",
            "Global Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(bestLocalMotivation)
        val expectedAsset = bestLocalMotivation.toAsset("file:///s-tier-waifus/ryuko.png")
        every { remoteAssetManager.resolveAsset(bestLocalMotivation) } returns expectedAsset.deepClonePolymorphic()
            .toOptional()


        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.get()).isEqualTo(expectedAsset)
    }
}
