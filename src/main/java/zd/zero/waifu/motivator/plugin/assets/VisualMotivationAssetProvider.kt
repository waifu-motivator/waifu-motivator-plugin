package zd.zero.waifu.motivator.plugin.assets

enum class WaifuAssetCategory {
    CELEBRATION,
    SMUG,
    MOTIVATION,
    WELCOMING,
    DEPARTURE,
    ENCOURAGEMENT,
    DISAPPOINTMENT // you don't want to disappoint your waifu now do you?
}

object VisualMotivationAssetProvider {

    fun createAssetByCategory(
        category: WaifuAssetCategory
    ): MotivationAsset {
        return when (category) {
            WaifuAssetCategory.CELEBRATION -> pickRandomCelebrationAsset()
            WaifuAssetCategory.DISAPPOINTMENT -> pickRandomDisappointmentAsset()
            else -> throw NotImplementedError("You can't use $category here.")
        }
    }

    fun pickAssetFromCategories(
        vararg categories: WaifuAssetCategory
    ): MotivationAsset =
        createAssetByCategory(categories.random())


    private fun pickRandomCelebrationAsset(): MotivationAsset =
        constructMotivation(
            TextAssetDefinitionService.getRandomCelebrationTextualAsset(),
            VisualAssetDefinitionService.getRandomCelebrationVisualAsset(),
            AudibleAssetDefinitionService.getRandomCelebrationAudibleAsset()
        )

    private fun pickRandomDisappointmentAsset(): MotivationAsset =
        constructMotivation(
            TextAssetDefinitionService.getRandomCelebrationTextualAsset(),
            VisualAssetDefinitionService.getRandomCelebrationVisualAsset(),
            AudibleAssetDefinitionService.getRandomCelebrationAudibleAsset()
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
