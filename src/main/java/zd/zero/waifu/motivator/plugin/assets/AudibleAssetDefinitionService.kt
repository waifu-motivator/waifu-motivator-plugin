package zd.zero.waifu.motivator.plugin.assets

object AudibleAssetDefinitionService {
    fun getRandomCelebrationAudibleAsset(): AudibleMotivationAssetDefinition {
        val soundFile = listOf("nice_nice_nice_nice.wav", "good_job.wav", "waoow.mp3").random()
        return AudibleMotivationAssetDefinition(
                soundFile,
                arrayOf(WaifuAssetCategory.CELEBRATION)
        )
    }
}
