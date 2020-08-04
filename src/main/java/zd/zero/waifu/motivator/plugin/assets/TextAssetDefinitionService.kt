package zd.zero.waifu.motivator.plugin.assets

object TextAssetDefinitionService {

    private val assetDefinitions: List<TextualMotivationAssetDefinition> = listOf(
        buildAssetDefinitions(WaifuAssetCategory.CELEBRATION,
            "Excellent!", "You did it!", "Way to go!", "Amazing work!", "You're the best!"),
        buildAssetDefinitions(WaifuAssetCategory.DISAPPOINTMENT,
            "Wwwwwwaaaaaaaaaaaaaaaaaaaaah", "OH MY GAH!",
            "Oh No!", "How could you do this?", "Why?", "I'm not mad, I'm disappointed"),
        buildAssetDefinitions(WaifuAssetCategory.SHOCKED,
            "Nani?!", "What the?!", "Oh No!", "Whyyy???"),
        buildAssetDefinitions(WaifuAssetCategory.SMUG,
            "Ohhhh yeaaahhh!!", "I meant to do that!",
            "I'm a pretty big deal", "Ain't nothing but a thing.",
            "Too easy!",
            "Awww yessss!!", "Nothing but skill!", "Get rekt, problems.", "They call that talent."),
        buildAssetDefinitions(WaifuAssetCategory.WELCOMING,
            "Heyyy!", "Welcome Back!", "Are you ready?!", "Nice to see you again!",
            "What's up?!", "Hiya!"
        )
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
