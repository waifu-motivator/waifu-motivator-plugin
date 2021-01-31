package zd.zero.waifu.motivator.plugin.test.tools

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.apache.http.impl.client.CloseableHttpClient
import zd.zero.waifu.motivator.plugin.assets.HttpClientFactory
import zd.zero.waifu.motivator.plugin.assets.LocalStorageService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object TestTools {

    fun getTestAssetPath(vararg extraDirectories: String): Path {
        val testAssetDirectory = Paths.get(".", "testAssets", *extraDirectories)
            .normalize()
            .toAbsolutePath()
        if (Files.exists(testAssetDirectory).not()) {
            Files.createDirectories(testAssetDirectory)
        }
        return testAssetDirectory
    }

    fun setUpMocksForManager() {
        mockkObject(HttpClientFactory)
        val mockHttpClient = mockk<CloseableHttpClient>()
        every { HttpClientFactory.createHttpClient() } returns mockHttpClient
        mockkObject(LocalStorageService)
    }

    fun tearDownMocksForPromotionManager() {
        unmockkObject(HttpClientFactory)
        unmockkObject(LocalStorageService)
    }
}
