package zd.zero.waifu.motivator.plugin.assets

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions
import org.junit.AfterClass
import org.junit.Test

import org.junit.Assert.*
import org.junit.BeforeClass
import zd.zero.waifu.motivator.plugin.test.tools.TestTools
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Paths
import java.util.*

class VisualMotivationAssetProviderTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUp() {
            TestTools.setUpMocksForAssetManager()
            mockkObject(TextAssetService)
            mockkObject(VisualAssetDefinitionService)
            mockkObject(AudibleAssetDefinitionService)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            TestTools.tearDownMocksForAssetManager()
            unmockkObject(TextAssetService)
            unmockkObject(VisualAssetDefinitionService)
            unmockkObject(AudibleAssetDefinitionService)
        }
    }

    @Test
    fun createAssetByCategoryShouldReturnEmptyWhenTextAssetDoesNotExist() {
        every { TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns Optional.empty()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            VisualMotivationAsset("Just", "Monika", ImageDimension(69, 420)).toOptional()
        every { AudibleAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            AudibleMotivationAsset(Paths.get("just", "monika")).toOptional()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)

        Assertions.assertThat(maybeMotivationAsset.isEmpty).isTrue()
    }

    @Test
    fun createAssetByCategoryShouldReturnEmptyWhenVisualAssetDoesNotExist() {
        every { TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            TextualMotivationAsset("Just Monika").toOptional()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            Optional.empty()
        every { AudibleAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            AudibleMotivationAsset(Paths.get("just", "monika")).toOptional()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)

        Assertions.assertThat(maybeMotivationAsset.isEmpty).isTrue()
    }

    @Test
    fun createAssetByCategoryShouldReturnEmptyWhenAudibleAssetDoesNotExist() {
        every { TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            TextualMotivationAsset("Just Monika").toOptional()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            VisualMotivationAsset("Just", "Monika", ImageDimension(69, 420)).toOptional()
        every { AudibleAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            Optional.empty()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)

        Assertions.assertThat(maybeMotivationAsset.isEmpty).isTrue()
    }

    @Test
    fun createAssetByCategoryShouldReturnExpectedMotivationWhenAllAssetsResolve() {
        every { TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            TextualMotivationAsset("Just Monika").toOptional()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            VisualMotivationAsset("Just", "Monika", ImageDimension(69, 420)).toOptional()
        every { AudibleAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            AudibleMotivationAsset(Paths.get("just", "monika")).toOptional()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)
            .get()

        Assertions.assertThat(maybeMotivationAsset.title).contains("Just Monika")
        Assertions.assertThat(maybeMotivationAsset.soundFilePath).isEqualTo(Paths.get("just", "monika"))
        Assertions.assertThat(maybeMotivationAsset.message).contains("Just", "Monika")
    }
}
