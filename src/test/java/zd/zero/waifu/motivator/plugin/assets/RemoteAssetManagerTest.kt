package zd.zero.waifu.motivator.plugin.assets

import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zd.zero.waifu.motivator.plugin.test.tools.TestTools
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Paths
import java.util.*

internal class FakeAssetManager :
    RemoteAssetManager<VisualMotivationAssetDefinition, VisualMotivationAsset>(
        AssetCategory.VISUAL
    ) {
    override fun convertToAsset(asset: VisualMotivationAssetDefinition, assetUrl: String): VisualMotivationAsset =
        VisualAssetManager.convertToAsset(asset, assetUrl)

    override fun convertToDefinitions(defJson: String): Optional<List<VisualMotivationAssetDefinition>> =
        VisualAssetManager.convertToDefinitions(defJson)
}

class RemoteAssetManagerTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUp() {
            TestTools.setUpMocksForAssetManager()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            TestTools.tearDownMocksForAssetManager()
        }
    }

    @Test
    fun getStatusShouldReturnBrokenWhenShitIsBroke() {
        every { AssetManager.resolveAssetUrl(AssetCategory.VISUAL, "assets.json") } returns
            Optional.empty()
        val fakeAssetManager = FakeAssetManager()

        assertThat(fakeAssetManager.status).isEqualTo(Status.BROKEN)
        assertThat(fakeAssetManager.supplyLocalAssetDefinitions()).isEmpty()
        assertThat(fakeAssetManager.supplyAssetDefinitions()).isEmpty()
    }

    @Test
    fun getStatusShouldReturnBrokenWhenAssetsFileIsntJson() {
        val localAssetDirectory = TestTools.getTestAssetPath()
        val assetsPath = Paths.get(localAssetDirectory.toString(), "visuals", "hard-to-swallow-pills.txt")

        every { LocalStorageService.getLocalAssetDirectory() } returns localAssetDirectory.toString().toOptional()

        every { AssetManager.resolveAssetUrl(AssetCategory.VISUAL, "assets.json") } returns
            assetsPath.toUri().toString().toOptional()

        val fakeAssetManager = FakeAssetManager()

        assertThat(fakeAssetManager.status).isEqualTo(Status.BROKEN)
        assertThat(fakeAssetManager.supplyLocalAssetDefinitions()).isEmpty()
        assertThat(fakeAssetManager.supplyAssetDefinitions()).isEmpty()
    }

    @Test
    fun getStatusShouldOkWhenAssetsMetaDataIsThere() {
        val localAssetDirectory = TestTools.getTestAssetPath()
        val assetsPath = Paths.get(localAssetDirectory.toString(), "visuals", "assets.json")

        every { LocalStorageService.getLocalAssetDirectory() } returns localAssetDirectory.toString().toOptional()

        every { AssetManager.resolveAssetUrl(AssetCategory.VISUAL, "assets.json") } returns
            assetsPath.toUri().toString().toOptional()

        val fakeAssetManager = FakeAssetManager()

        assertThat(fakeAssetManager.status).isEqualTo(Status.OK)
        assertThat(fakeAssetManager.supplyAssetDefinitions()).extracting("path")
            .containsExactlyInAnyOrder(
                "waiting/kanna_bored.gif",
                "waiting/konata_bored_one.gif",
                "waiting/misato_bored_one.gif",
                "waiting/ryuko_waiting.gif"
            )

        assertThat(fakeAssetManager.supplyLocalAssetDefinitions()).extracting("path")
            .containsExactlyInAnyOrder(
                "waiting/konata_bored_one.gif",
                "waiting/ryuko_waiting.gif"
            )
    }
}
