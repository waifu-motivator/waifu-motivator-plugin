package zd.zero.waifu.motivator.plugin.assets

import kotlin.random.Random
import com.intellij.openapi.util.SystemInfo

enum class WaifuAssetCategory {
    CELEBRATION,
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
    ): MotivationAsset {
        return when (category) {
            WaifuAssetCategory.CELEBRATION,
            WaifuAssetCategory.DISAPPOINTMENT,
            WaifuAssetCategory.SHOCKED,
            WaifuAssetCategory.SMUG,
            WaifuAssetCategory.WELCOMING
            -> pickRandomAssetByCategory(
                category
            )
            else -> throw NotImplementedError("You can't use $category here.")
        }
    }

    fun pickAssetFromCategories(
        vararg categories: WaifuAssetCategory
    ): MotivationAsset =
        createAssetByCategory(categories.random(random))

    private fun pickRandomAssetByCategory(category: WaifuAssetCategory): MotivationAsset =
        constructMotivation(
            TextAssetDefinitionService.getRandomAssetByCategory(category),
            VisualAssetDefinitionService.getRandomAssetByCategory(category),
            AudibleAssetDefinitionService.getRandomAssetByCategory(category)
        )

    private fun constructMotivation(
        textualAssetDefinition: TextualMotivationAssetDefinition,
        visualAssetDefinition: VisualMotivationAssetDefinition,
        audibleAssetDefinition: AudibleMotivationAssetDefinition
    ): MotivationAsset =
        MotivationAsset(
            "&nbsp;&nbsp;&nbsp;${textualAssetDefinition.title}",
            """
                <div style="margin: 5px 5px 5px 10px;${getExtraStyles(visualAssetDefinition.imageDimensions)}" >
                    <img src='${visualAssetDefinition.imagePath}' alt='${visualAssetDefinition.imageAlt}'/>
                </div>
            """.trimIndent(),
            audibleAssetDefinition.soundFile,
            arrayOf()
        )

    private fun getExtraStyles(imageDimensions: ImageDimension): String =
        if (!SystemInfo.isMac) {
            "width: ${reduceSize(imageDimensions.width, 0.75)}px;" +
                "height: ${reduceSize(imageDimensions.height, 0.85)}px"
        } else {
            ""
        }

    private fun reduceSize(dimensionToReduce: Int, modifier: Double) =
        (dimensionToReduce.toDouble() * modifier).toInt()
}
