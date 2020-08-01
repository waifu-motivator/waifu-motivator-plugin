package zd.zero.waifu.motivator.plugin.assets

object TextAssetDefinitionService {

    fun getRandomCelebrationTextualAsset(): TextualMotivationAssetDefinition {
        val messages = listOf("Excellent!", "You did it!", "Way to go!", "Amazing work!", "You're the best!")
        val title = messages.random()
        return TextualMotivationAssetDefinition(
                title,
                messages.random(),
                arrayOf(WaifuAssetCategory.CELEBRATION)
        )
    }

}
