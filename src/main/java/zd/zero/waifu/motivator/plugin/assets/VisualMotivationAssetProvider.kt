package zd.zero.waifu.motivator.plugin.assets

enum class WaifuAssetCategory {
    CELEBRATION,
    MOTIVATION,
    ENCOURAGEMENT,
    DISAPPOINTMENT // you don't want to disappoint your waifu now do you?
}

object VisualMotivationAssetProvider {

    fun createAssetByCategory(
        category: WaifuAssetCategory
    ): MotivationAsset {
        return when (category) {
            WaifuAssetCategory.CELEBRATION -> pickRandomCelebrationAsset()
            else -> throw NotImplementedError("You can't use $category here.")
        }
    }

    private fun pickRandomCelebrationAsset(): MotivationAsset {
        val visualAssetDefinition = AssetDefinitionService.getRandomCelebrationVisualAsset()
        val audibleAssetDefinition = AssetDefinitionService.getRandomCelebrationAudibleAsset()
        val textualAssetDefinition = AssetDefinitionService.getRandomCelebrationTextualAsset()
        return MotivationAsset(
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
}
