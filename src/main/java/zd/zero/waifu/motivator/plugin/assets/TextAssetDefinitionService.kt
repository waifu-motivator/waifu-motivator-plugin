package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TextAssetDefinitionService : RemoteAssetDefinitionService<TextualMotivationAssetDefinition>(
    TextAssetManager
) {
    private val gson = Gson()
    private val categoryToTextDefinitions: MutableMap<WaifuAssetCategory, List<TextualMotivationAsset>> =
        ConcurrentHashMap()

    fun pickRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): TextualMotivationAsset {
        val listOfAssets = getListOfAssets(waifuAssetCategory)
        return listOfAssets.random(random)
    }

    private fun getListOfAssets(waifuAssetCategory: WaifuAssetCategory): List<TextualMotivationAsset> =
        if (categoryToTextDefinitions.containsKey(waifuAssetCategory)) {
            categoryToTextDefinitions[waifuAssetCategory]!!
        } else {
            remoteAssetManager.supplyAssetDefinitions().stream()
                .filter { it.categories.contains(waifuAssetCategory) }
                .findFirst()
                .map {
                    Files.newBufferedReader(
                        Paths.get(URI(remoteAssetManager.resolveAsset(it).path))
                    ).use { reader ->
                        gson.fromJson<List<TextualMotivationAsset>>(
                            reader,
                            object : TypeToken<List<TextualMotivationAsset>>() {}.type
                        )
                    }
                }
                .orElseGet {
                    Collections.emptyList<TextualMotivationAsset>()
                }
        }
}
