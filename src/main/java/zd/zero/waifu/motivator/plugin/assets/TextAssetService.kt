package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Files
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

object TextAssetService {
    private val gson = Gson()
    private val categoryToTextDefinitions: MutableMap<WaifuAssetCategory, List<TextualMotivationAsset>> =
        ConcurrentHashMap()
    private val random = Random(System.currentTimeMillis())

    fun pickRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): Optional<TextualMotivationAsset> =
        getListOfAssets(waifuAssetCategory)
            .map {
                it.random(random)
            }

    private fun getListOfAssets(waifuAssetCategory: WaifuAssetCategory): Optional<List<TextualMotivationAsset>> =
        if (categoryToTextDefinitions.containsKey(waifuAssetCategory)) {
            categoryToTextDefinitions[waifuAssetCategory]!!.toOptional()
        } else {
            TextAssetDefinitionService.getRandomAssetByCategory(waifuAssetCategory)
                .map { assetPackage ->
                    val motivationAssets = Files.newBufferedReader(
                        assetPackage.packagePath
                    ).use { reader ->
                        gson.fromJson<List<TextualMotivationAsset>>(
                            reader,
                            object : TypeToken<List<TextualMotivationAsset>>() {}.type
                        )
                    }

                    categoryToTextDefinitions[waifuAssetCategory] = motivationAssets

                    motivationAssets
                }
        }
}
