package zd.zero.waifu.motivator.plugin.test.tools

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.apache.http.impl.client.CloseableHttpClient
import zd.zero.waifu.motivator.plugin.assets.AssetManager
import zd.zero.waifu.motivator.plugin.assets.HttpClientFactory
import zd.zero.waifu.motivator.plugin.assets.LocalStorageService
import java.nio.file.Path
import java.nio.file.Paths

object TestTools {

    fun getTestAssetPath(): Path =
        Paths.get(".", "src", "test", "resources").toAbsolutePath()

    fun setUpMocksForAssetManager() {
        mockkObject(HttpClientFactory)
        val mockHttpClient = mockk<CloseableHttpClient>()
        every { HttpClientFactory.createHttpClient() } returns mockHttpClient
        mockkObject(AssetManager)
        mockkObject(LocalStorageService)
    }

    fun tearDownMocksForAssetManager() {
        unmockkObject(HttpClientFactory)
        unmockkObject(AssetManager)
        unmockkObject(LocalStorageService)
    }
}
