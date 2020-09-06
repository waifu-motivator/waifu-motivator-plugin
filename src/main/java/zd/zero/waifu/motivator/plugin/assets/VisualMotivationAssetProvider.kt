package zd.zero.waifu.motivator.plugin.assets

import zd.zero.waifu.motivator.plugin.tools.allOf
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.*
import kotlin.random.Random

enum class WaifuAssetCategory {
    FRUSTRATION,
    ENRAGED,
    CELEBRATION,
    HAPPY,
    SMUG,
    WAITING,
    MOTIVATION,
    WELCOMING,
    DEPARTURE,
    ENCOURAGEMENT,
    TSUNDERE,
    SHOCKED,
    DISAPPOINTMENT // you don't want to disappoint your waifu now do you?
}

object VisualMotivationAssetProvider {

    private val random = Random(System.currentTimeMillis())

    fun createAssetByCategory(
        category: WaifuAssetCategory
    ): Optional<MotivationAsset> {
        return when (category) {
            WaifuAssetCategory.CELEBRATION,
            WaifuAssetCategory.DISAPPOINTMENT,
            WaifuAssetCategory.SHOCKED,
            WaifuAssetCategory.SMUG,
            WaifuAssetCategory.WAITING,
            WaifuAssetCategory.WELCOMING,
            WaifuAssetCategory.ENRAGED,
            WaifuAssetCategory.FRUSTRATION,
            WaifuAssetCategory.HAPPY
            -> pickRandomAssetByCategory(
                category
            )
            else -> throw NotImplementedError("You can't use $category here.")
        }
    }

    fun pickAssetFromCategories(
        vararg categories: WaifuAssetCategory
    ): Optional<MotivationAsset> =
        categories.toOptional()
            .filter { it.isNotEmpty() }
            .flatMap { createAssetByCategory(it.random(random)) }

    private fun pickRandomAssetByCategory(category: WaifuAssetCategory): Optional<MotivationAsset> =
        allOf(
            TextAssetService.pickRandomAssetByCategory(category),
            VisualAssetDefinitionService.getRandomAssetByCategory(category),
            AudibleAssetDefinitionService.getRandomAssetByCategory(category)
        ).map {
            constructMotivation(it.first, it.second, it.third)
        }

    private fun constructMotivation(
        textualAssetDefinition: TextualMotivationAsset,
        visualAssetDefinition: VisualMotivationAsset,
        audibleAssetDefinition: AudibleMotivationAsset
    ): MotivationAsset =
        MotivationAsset(
            "&nbsp;&nbsp;&nbsp;${textualAssetDefinition.title}",
            """
                <div style="margin: 5px 5px 5px 10px;${getExtraStyles(visualAssetDefinition.imageDimensions)}" >
                    <img src='${visualAssetDefinition.filePath}' alt='${visualAssetDefinition.imageAlt}'/>
                </div>
            """.trimIndent(),
            audibleAssetDefinition.soundFilePath,
            arrayOf()
        )

    private fun getExtraStyles(imageDimensions: ImageDimension): String =
        "width: ${reduceSize(imageDimensions.width, 0.75)}px;" +
            "height: ${reduceSize(imageDimensions.height, 0.85)}px"

    private fun reduceSize(dimensionToReduce: Int, modifier: Double) =
        (dimensionToReduce.toDouble() * modifier).toInt()
}
