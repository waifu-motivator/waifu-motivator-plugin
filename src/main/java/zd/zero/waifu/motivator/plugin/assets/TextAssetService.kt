package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

object TextAssetService {
    private val gson = Gson()
    private val categoryToTextDefinitions: MutableMap<WaifuAssetCategory, List<TextualMotivationAsset>> =
            ConcurrentHashMap()
    private val random = Random(System.currentTimeMillis())

    fun pickRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): TextualMotivationAsset {
        val listOfAssets = getListOfAssets(waifuAssetCategory)
        return listOfAssets.random(random)
    }

    private fun getListOfAssets(waifuAssetCategory: WaifuAssetCategory): List<TextualMotivationAsset> =
        if (categoryToTextDefinitions.containsKey(waifuAssetCategory)) {
            categoryToTextDefinitions[waifuAssetCategory]!!
        } else {
            Files.newBufferedReader(
                    Paths.get(URI(TextAssetDefinitionService.getRandomAssetByCategory(waifuAssetCategory).path))
            ).use { reader ->
                gson.fromJson<List<TextualMotivationAsset>>(
                    reader,
                    object : TypeToken<List<TextualMotivationAsset>>() {}.type
                )
            }
        }
}
