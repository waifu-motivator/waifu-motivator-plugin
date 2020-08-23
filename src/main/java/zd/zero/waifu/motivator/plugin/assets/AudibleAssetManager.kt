package zd.zero.waifu.motivator.plugin.assets

object AudibleAssetManager : RemoteAssetManager<AudibleMotivationAssetDefinition>(
    AssetCategory.AUDIBLE,
    emptyList()
) {
    override fun applyAssetUrl(
        visualAsset: AudibleMotivationAssetDefinition,
        assetUrl: String
    ): AudibleMotivationAssetDefinition =
        visualAsset.copy(path = assetUrl)
}
