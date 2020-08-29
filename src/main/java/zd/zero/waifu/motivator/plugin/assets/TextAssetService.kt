package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.tools.ExceptionTools
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.io.BufferedReader
import java.nio.file.Files
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

object TextAssetService {
    private val gson = Gson()
    private val log: Logger? = Logger.getInstance(this::class.java)
    private val categoryToTextDefinitions: MutableMap<WaifuAssetCategory, List<TextualMotivationAsset>> =
        ConcurrentHashMap()
    private val random = Random(System.currentTimeMillis())

    fun pickRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): Optional<TextualMotivationAsset> =
        getListOfAssets(waifuAssetCategory)
            .filter { it.isNotEmpty() }
            .map {
                it.random(random)
            }

    private fun getListOfAssets(waifuAssetCategory: WaifuAssetCategory): Optional<List<TextualMotivationAsset>> =
        if (categoryToTextDefinitions.containsKey(waifuAssetCategory)) {
            categoryToTextDefinitions[waifuAssetCategory]!!.toOptional()
        } else {
            TextAssetDefinitionService.getRandomAssetByCategory(waifuAssetCategory)
                .flatMap { assetPackage ->
                    attemptToReadAsset(assetPackage)
                }
                .map {
                    categoryToTextDefinitions[waifuAssetCategory] = it
                    it
                }
        }

    private fun attemptToReadAsset(assetPackage: TextualMotivationAssetPackage): Optional<List<TextualMotivationAsset>> =
        ExceptionTools.runSafely({
            val motivationAssets = Files.newBufferedReader(
                assetPackage.packagePath
            ).use { reader ->
                readJson(reader, assetPackage)
            }
            motivationAssets
        }) {
            log?.warn("Unable to read text asset ${assetPackage.packagePath}", it)
        }.flatMap { it }

    private fun readJson(
        reader: BufferedReader,
        assetPackage: TextualMotivationAssetPackage
    ): Optional<List<TextualMotivationAsset>> =
        ExceptionTools.runSafely({
            val fromJson = gson.fromJson<List<TextualMotivationAsset>>(
                reader,
                object : TypeToken<List<TextualMotivationAsset>>() {}.type
            )
            fromJson
        }) {
            log?.warn("Unable to parse asset ${assetPackage.packagePath}", it)
        }
}
