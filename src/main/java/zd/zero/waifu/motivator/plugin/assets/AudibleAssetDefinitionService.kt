package zd.zero.waifu.motivator.plugin.assets

object AudibleAssetDefinitionService {

    private val assetDefinitions: List<AudibleMotivationAssetDefinition> = listOf(
        buildAssetDefinitions(WaifuAssetCategory.CELEBRATION,
            "nice_nice_nice_nice.wav", "good_job.wav", "waoow.mp3"),
        buildAssetDefinitions(WaifuAssetCategory.SMUG,
            "nice_nice_nice_nice.wav", "good_job.wav", "waoow.mp3"),
        buildAssetDefinitions(WaifuAssetCategory.DISAPPOINTMENT,
            "ganbatte_onii_chan.wav", "waaaah.mp3", "ohmygah.mp3"),
        buildAssetDefinitions(WaifuAssetCategory.SHOCKED,
            "ganbatte_onii_chan.wav", "waaaah.mp3", "ohmygah.mp3"),
        buildAssetDefinitions(WaifuAssetCategory.WELCOMING,
            "tuturuu_mayushi_desu.wav", "waoow.mp3")
    ).flatten()

    private fun buildAssetDefinitions(
        category: WaifuAssetCategory,
        vararg assets: String
    ) = assets.map {
        AudibleMotivationAssetDefinition(
            it, arrayOf(category)
        )
    }

    fun getRandomAssetByCategory(waifuAssetCategory: WaifuAssetCategory) =
        assetDefinitions.filter { it.categories.contains(waifuAssetCategory) }
            .random()
}
