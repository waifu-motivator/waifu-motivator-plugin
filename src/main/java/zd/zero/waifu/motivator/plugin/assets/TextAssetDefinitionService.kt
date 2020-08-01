package zd.zero.waifu.motivator.plugin.assets

object TextAssetDefinitionService {

    private val assetDefinitions: List<TextualMotivationAssetDefinition> = listOf(
        buildAssetDefinitions(WaifuAssetCategory.CELEBRATION,
            "Excellent!", "You did it!", "Way to go!", "Amazing work!", "You're the best!"),
        buildAssetDefinitions(WaifuAssetCategory.DISAPPOINTMENT,
            "Wwwwwwaaaaaaaaaaaaaaaaaaaaah", "OH MY GAH!",
            "Oh No!", "How could you do this?", "Why?", "I'm not mad, I'm disappointed")
    ).flatten()

    private fun buildAssetDefinitions(
        category: WaifuAssetCategory,
        vararg assets: String
    ) = assets.map {
        TextualMotivationAssetDefinition(
            it, it, arrayOf(category)
        )
    }

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory): TextualMotivationAssetDefinition =
        assetDefinitions.filter { it.categories.contains(waifuAssetCategory) }
            .random()
}
