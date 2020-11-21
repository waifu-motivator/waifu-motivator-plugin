package zd.zero.waifu.motivator.plugin.assets

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zd.zero.waifu.motivator.plugin.test.tools.TestTools
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Paths
import java.util.Optional
import java.util.UUID

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
    fun `createAssetByCategory should return empty when text does not resolve`() {
        every { TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns Optional.empty()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            VisualMotivationAsset("Just", "Monika", ImageDimension(69, 420)).toOptional()
        every { AudibleAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            AudibleMotivationAsset(Paths.get("just", "monika")).toOptional()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)

        assertThat(maybeMotivationAsset.isEmpty).isTrue()
    }

    @Test
    fun `createAssetByCategory should return empty when visual does not resolve`() {
        every { TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            TextualMotivationAsset("Just Monika").toOptional()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            Optional.empty()
        every { AudibleAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            AudibleMotivationAsset(Paths.get("just", "monika")).toOptional()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)

        assertThat(maybeMotivationAsset.isEmpty).isTrue()
    }

    @Test
    fun `createAssetByCategory should return empty when audio does not exist`() {
        every { TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            TextualMotivationAsset("Just Monika").toOptional()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            VisualMotivationAsset("Just", "Monika", ImageDimension(69, 420)).toOptional()
        every { AudibleAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            Optional.empty()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)

        assertThat(maybeMotivationAsset.isEmpty).isTrue()
    }

    @Test
    fun `createAssetByCategory should return expected motivation when all assets resolve`() {
        every { TextAssetService.getRandomUngroupedAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            TextualMotivationAsset("Just Monika").toOptional()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            VisualMotivationAsset("Just", "Monika", ImageDimension(69, 420)).toOptional()
        every { AudibleAssetDefinitionService.getRandomUngroupedAssetByCategory(WaifuAssetCategory.CELEBRATION) } returns
            AudibleMotivationAsset(Paths.get("just", "monika")).toOptional()

        val maybeMotivationAsset = VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.CELEBRATION)
            .get()

        assertThat(maybeMotivationAsset.title).contains("Just Monika")
        assertThat(maybeMotivationAsset.soundFilePath).isEqualTo(Paths.get("just", "monika"))
        assertThat(maybeMotivationAsset.message).contains("Just", "Monika")
    }

    @Test
    fun `createAssetByCategory should return related group assets`() {
        val groupId = UUID.randomUUID()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.MOCKING) } returns
            VisualMotivationAsset(
                "/o_kawaii_koto.gif",
                "O Kawaii Koto",
                ImageDimension(69, 420),
                groupId
            ).toOptional()
        every {
            TextAssetService.getAssetByGroupId(
                groupId,
                WaifuAssetCategory.MOCKING
            )
        } returns TextualMotivationAsset("O Kawaii Koto").toOptional()
        every {
            AudibleAssetDefinitionService.getAssetByGroupId(
                groupId,
                WaifuAssetCategory.MOCKING
            )
        } returns AudibleMotivationAsset(Paths.get("/o_kawaii_koto.mp3")).toOptional()

        val maybeMotivationAsset =
            VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.MOCKING)
                .get()

        assertThat(maybeMotivationAsset.title).contains("O Kawaii Koto")
        assertThat(maybeMotivationAsset.soundFilePath).isEqualTo(Paths.get("/o_kawaii_koto.mp3"))
        assertThat(maybeMotivationAsset.message).contains("o_kawaii_koto.gif")
    }

    @Test
    fun `createAssetByCategory should return empty when group text does not resolve`() {
        val groupId = UUID.randomUUID()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.MOCKING) } returns
            VisualMotivationAsset(
                "/o_kawaii_koto.gif",
                "O Kawaii Koto",
                ImageDimension(69, 420),
                groupId
            ).toOptional()
        every {
            TextAssetService.getAssetByGroupId(
                groupId,
                WaifuAssetCategory.MOCKING
            )
        } returns Optional.empty()
        every {
            AudibleAssetDefinitionService.getAssetByGroupId(
                groupId,
                WaifuAssetCategory.MOCKING
            )
        } returns AudibleMotivationAsset(Paths.get("/o_kawaii_koto.mp3")).toOptional()

        assertThat(
            VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.MOCKING)
                .isEmpty
        ).isTrue()
    }

    @Test
    fun `createAssetByCategory should return empty when group audio does not resolve`() {
        val groupId = UUID.randomUUID()
        every { VisualAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.MOCKING) } returns
            VisualMotivationAsset(
                "/o_kawaii_koto.gif",
                "O Kawaii Koto",
                ImageDimension(69, 420),
                groupId
            ).toOptional()
        every {
            TextAssetService.getAssetByGroupId(
                groupId,
                WaifuAssetCategory.MOCKING
            )
        } returns TextualMotivationAsset("O Kawaii Koto").toOptional()
        every {
            AudibleAssetDefinitionService.getAssetByGroupId(
                groupId,
                WaifuAssetCategory.MOCKING
            )
        } returns Optional.empty()

        assertThat(
            VisualMotivationAssetProvider.createAssetByCategory(WaifuAssetCategory.MOCKING)
                .isEmpty
        ).isTrue()
    }
}
