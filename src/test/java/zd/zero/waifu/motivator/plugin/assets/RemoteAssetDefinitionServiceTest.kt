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
) : RemoteAssetDefinitionService<VisualMotivationAssetDefinition, VisualMotivationAsset>(assetManager) {
    override fun downloadNewAsset(waifuAssetCategory: WaifuAssetCategory) {
        // do nothing
    }
}

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
    fun `get random asset by category should return empty when no assets`() {
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf()
        every { remoteAssetManager.supplyRemoteAssetDefinitions() } returns listOf()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.isEmpty).isTrue()
    }

    @Test
    fun `get random asset by category should return local when no remote assets`() {
        every { remoteAssetManager.supplyRemoteAssetDefinitions() } answers { callOriginal() }

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
    fun `get random asset by category should return empty when unable to fetch remote asset with no local assets`() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyRemoteAssetDefinitions() } returns listOf()
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns Optional.empty()
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.isEmpty).isTrue()
    }

    @Test
    fun `get random asset by category should return remote when available`() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyRemoteAssetDefinitions() } returns listOf(bestMotivation)
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf()

        val expectedAsset = bestMotivation.toAsset("file:///s-tier-waifus/ryuko.png")
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns expectedAsset.deepClonePolymorphic()
            .toOptional()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.get()).isEqualTo(expectedAsset)
    }

    @Test
    fun `get random asset by category should return empty when unable to fetch remote asset with no matching local assets`() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyRemoteAssetDefinitions() } returns listOf()
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
    fun `get random asset by category should return empty when local asset is not local`() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.resolveAsset(bestMotivation) } returns Optional.empty()

        val bestLocalMotivation = VisualMotivationAssetDefinition(
            "Local Ryuko",
            "Local Ryuko",
            "Global Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(bestLocalMotivation)
        every { remoteAssetManager.resolveAsset(bestLocalMotivation) } returns Optional.empty()

        val randomAsset = fakeVisualAssetDefinitionService.getRandomAssetByCategory(
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(randomAsset.isEmpty).isTrue()
    }

    @Test
    fun `get random asset by category should return local asset remote asset is not available`() {
        val bestMotivation = VisualMotivationAssetDefinition(
            "Ryuko",
            "Ryuko",
            "Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION)
        )
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

    @Test
    fun `getAssetByGroupId should return asset when available locally`() {
        val groupId = UUID.randomUUID()

        val bestLocalMotivation = VisualMotivationAssetDefinition(
            "Local Ishtar",
            "Local Ishtar",
            "Global Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION),
            groupId
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(bestLocalMotivation)
        val expectedAsset = bestLocalMotivation.toAsset("file:///s-tier-waifus/ishtar.png")
        every { remoteAssetManager.resolveAsset(bestLocalMotivation) } returns expectedAsset.deepClonePolymorphic()
            .toOptional()

        val groupAsset = fakeVisualAssetDefinitionService.getAssetByGroupId(
            groupId,
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(groupAsset.get()).isEqualTo(expectedAsset)
    }

    @Test
    fun `getAssetByGroupId should download group asset when cant find requested group`() {
        val groupId = UUID.randomUUID()

        val notTheMotivationYouAreLookingFor = VisualMotivationAssetDefinition(
            "Local Aqua",
            "Local Aqua",
            "Aqua",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION),
            UUID.randomUUID()
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(notTheMotivationYouAreLookingFor)

        val remoteAsset = VisualMotivationAssetDefinition(
            "Local Ishtar",
            "Local Ishtar",
            "Global Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION),
            groupId
        )
        every { remoteAssetManager.supplyRemoteAssetDefinitions() } returns listOf(remoteAsset)
        val expectedAsset = remoteAsset.toAsset("file:///s-tier-waifus/ishtar.png")
        every { remoteAssetManager.resolveAsset(remoteAsset) } returns expectedAsset.deepClonePolymorphic()
            .toOptional()

        val groupAsset = fakeVisualAssetDefinitionService.getAssetByGroupId(
            groupId,
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(groupAsset.get()).isEqualTo(expectedAsset)
    }

    @Test
    fun `getAssetByGroupId should pick random asset when cant find requested group locally or remote`() {
        val groupId = UUID.randomUUID()

        val notTheMotivationYouAreLookingFor = VisualMotivationAssetDefinition(
            "Local Aqua",
            "Local Aqua",
            "Aqua",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION),
            UUID.randomUUID()
        )
        every { remoteAssetManager.supplyLocalAssetDefinitions() } returns setOf(notTheMotivationYouAreLookingFor)

        val remoteAsset = VisualMotivationAssetDefinition(
            "Local Ishtar",
            "Local Ishtar",
            "Global Best Girl",
            ImageDimension(69, 420),
            listOf(WaifuAssetCategory.MOTIVATION),
            UUID.randomUUID()
        )
        every { remoteAssetManager.supplyRemoteAssetDefinitions() } returns listOf(remoteAsset)
        val expectedAsset = remoteAsset.toAsset("file:///b-tier-waifus/aqua.png")
        every { remoteAssetManager.resolveAsset(notTheMotivationYouAreLookingFor) } returns expectedAsset.deepClonePolymorphic()
            .toOptional()

        val groupAsset = fakeVisualAssetDefinitionService.getAssetByGroupId(
            groupId,
            WaifuAssetCategory.MOTIVATION
        )

        assertThat(groupAsset.get()).isEqualTo(expectedAsset)
    }
}
