package zd.zero.waifu.motivator.plugin.assets

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
        createAssetByCategory(categories.random())

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
            textualAssetDefinition.title,
            """
                 <img src='${visualAssetDefinition.imagePath}' alt='${visualAssetDefinition.imageAlt}' width='256' />
                 <br><br><br><br><br><br><br><br><br><br>
                 <p>${textualAssetDefinition.message}</p>
                """.trimIndent(),
            audibleAssetDefinition.soundFile,
            arrayOf()
        )
}
