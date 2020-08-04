package zd.zero.waifu.motivator.plugin.assets

import kotlin.random.Random

object TextAssetDefinitionService {

    private val ranbo = Random(System.currentTimeMillis())

    private val assetDefinitions: List<TextualMotivationAssetDefinition> = listOf(
        buildAssetDefinitions(WaifuAssetCategory.CELEBRATION,
            "Excellent!", "You did it!", "Way to go!", "Amazing work!", "You're the best!"),
        buildAssetDefinitions(WaifuAssetCategory.DISAPPOINTMENT,
            "Wwwwwwaaaaaaaaaaaaaaaaaaaaah", "OH MY GAH!",
            "Oh No!", "How could you do this?", "Why?", "Why did you do that??"
        ),
        buildAssetDefinitions(WaifuAssetCategory.SHOCKED,
            "Nani?!", "What the?!", "Oh No!", "Whyyy???", "The horror!",
            "OH MY GAH!!", "How could this happen?!", "Why did you do that??",
            "Hurry quick fix it!"
        ),
        buildAssetDefinitions(WaifuAssetCategory.SMUG,
            "Ohhhh yeaaahhh!!", "I meant to do that!",
            "You're a pretty big deal..", "Ain't nothing but a thing.",
            "Too easy!", "Make it more difficult.",
            "Awww yessss!!", "Nothing but skill!", "Get rekt, problems.", "They call that talent."),
        buildAssetDefinitions(WaifuAssetCategory.WELCOMING,
            "Heyyy!", "Welcome Back!", "Nice to see you again!",
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
            .random(ranbo)
}
