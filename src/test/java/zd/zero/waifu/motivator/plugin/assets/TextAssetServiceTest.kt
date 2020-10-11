package zd.zero.waifu.motivator.plugin.assets

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.AfterClass
import org.junit.Test

import org.junit.BeforeClass
import zd.zero.waifu.motivator.plugin.test.tools.TestTools
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Paths
import java.util.*

class TextAssetServiceTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUp() {
            TestTools.setUpMocksForAssetManager()
            mockkObject(TextAssetDefinitionService)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            TestTools.tearDownMocksForAssetManager()
            unmockkObject(TextAssetDefinitionService)
        }
    }

    @Test
    fun `getAssetByGroupId should return expected asset`() {
        val groupId = UUID.randomUUID()
        every { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.SHOCKED) } returns
            TextualMotivationAssetPackage(
                Paths.get(TestTools.getTestAssetPath().toString(), "text", "shocked.json")
            ).toOptional()

        Assertions.assertThat(TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.SHOCKED).get().title)
            .isNotBlank()
        Assertions.assertThat(TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.SHOCKED).get().title)
            .isNotBlank()

        verify(exactly = 1) { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.SHOCKED) }
    }


    @Test
    fun `pickRandomAssetByCategory should return expected asset`() {
        every { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.SHOCKED) } returns
            TextualMotivationAssetPackage(
                Paths.get(TestTools.getTestAssetPath().toString(), "text", "shocked.json")
            ).toOptional()

        val groupId = UUID.fromString("e5feeb14-69e5-4596-8d84-c564c20a9715")
        Assertions.assertThat(TextAssetService.getAssetByGroupId(groupId, WaifuAssetCategory.SHOCKED).get().title)
            .isEqualTo("Dafuq?!")
        Assertions.assertThat(TextAssetService.getAssetByGroupId(groupId, WaifuAssetCategory.SHOCKED).get().title)
            .isEqualTo("Dafuq?!")

        verify(exactly = 1) { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.SHOCKED) }
    }

    @Test
    fun `pickRandomAssetByCategory should fallback to asset of same category`() {
        every { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.SHOCKED) } returns
            TextualMotivationAssetPackage(
                Paths.get(TestTools.getTestAssetPath().toString(), "text", "shocked.json")
            ).toOptional()

        val groupId = UUID.randomUUID()
        Assertions.assertThat(TextAssetService.getAssetByGroupId(groupId, WaifuAssetCategory.SHOCKED).get().title)
            .isNotBlank()
        Assertions.assertThat(TextAssetService.getAssetByGroupId(groupId, WaifuAssetCategory.SHOCKED).get().title)
            .isNotBlank()

        verify(exactly = 1) { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.SHOCKED) }
    }

    @Test
    fun `pickRandomAssetByCategory should return empty when no assets`() {
        every { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.TSUNDERE) } returns
            TextualMotivationAssetPackage(
                Paths.get(TestTools.getTestAssetPath().toString(), "text", "empty.json")
            ).toOptional()

        Assertions.assertThat(TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.TSUNDERE).isEmpty)
            .isTrue()
        Assertions.assertThat(TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.TSUNDERE).isEmpty)
            .isTrue()

        verify(exactly = 1) { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.TSUNDERE) }
    }

    @Test
    fun `pickRandomAssetByCategory should return empty when not json`() {
        every { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.WELCOMING) } returns
            TextualMotivationAssetPackage(
                Paths.get(TestTools.getTestAssetPath().toString(), "text", "facts.txt")
            ).toOptional()

        Assertions.assertThat(TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.WELCOMING).isEmpty)
            .isTrue()
        Assertions.assertThat(TextAssetService.pickRandomAssetByCategory(WaifuAssetCategory.WELCOMING).isEmpty)
            .isTrue()

        verify(exactly = 2) { TextAssetDefinitionService.getRandomAssetByCategory(WaifuAssetCategory.WELCOMING) }
    }
}
