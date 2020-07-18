package zd.zero.waifu.motivator.plugin.alert

interface AssetProvider {
    fun pickRandomByCategory(category: WaifuMotivatorAlertAssetCategory): MotivationAsset
}
