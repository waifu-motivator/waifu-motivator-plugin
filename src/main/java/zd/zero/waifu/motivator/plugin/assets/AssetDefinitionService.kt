package zd.zero.waifu.motivator.plugin.assets

object AssetDefinitionService {

    fun getRandomCelebrationVisualAsset(): VisualMotivationAssetDefinition {
        val celebrationAsset = VisualAssetManager.supplyAssetDefinitions()
            .filter {
                it.categories.contains(WaifuAssetCategory.CELEBRATION)
            }.random()
        return VisualAssetManager.resolveAsset(celebrationAsset)
    }

    fun getRandomCelebrationAudibleAsset(): AudibleMotivationAssetDefinition {
        // todo: use remote/local assets
        val soundFile = listOf("nice_nice_nice_nice.wav", "good_job.wav", "waoow.mp3").random()
        return AudibleMotivationAssetDefinition(
            soundFile,
            arrayOf(WaifuAssetCategory.CELEBRATION)
        )
    }

    fun getRandomCelebrationTextualAsset(): TextualMotivationAssetDefinition {
        // todo: use remote/local assets
        val messages = listOf("Excellent!", "You did it!", "Way to go!", "Amazing work!", "You're the best!")
        val title = messages.random()
        return TextualMotivationAssetDefinition(
            title,
            messages.random(),
            arrayOf(WaifuAssetCategory.CELEBRATION)
        )
    }
}
