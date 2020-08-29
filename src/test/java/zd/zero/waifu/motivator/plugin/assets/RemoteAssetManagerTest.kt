package zd.zero.waifu.motivator.plugin.assets

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.apache.http.impl.client.CloseableHttpClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import zd.zero.waifu.motivator.plugin.assets.TestTools.setUpMocksForAssetManager
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Paths
import java.util.*

object TestTools {
    fun setUpMocksForAssetManager() {
        mockkObject(HttpClientFactory)
        val mockHttpClient = mockk<CloseableHttpClient>()
        every { HttpClientFactory.createHttpClient() } returns mockHttpClient
        mockkObject(AssetManager)
        mockkObject(LocalStorageService)
    }
}

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

    @Before
    fun setUp() {
        setUpMocksForAssetManager()
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
        val localAssetDirectory = Paths.get(".", "src", "test", "resources").toAbsolutePath()
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
        val localAssetDirectory = Paths.get(".", "src", "test", "resources").toAbsolutePath()
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
