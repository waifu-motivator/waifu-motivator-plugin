package zd.zero.waifu.motivator.plugin.alert

interface AssetDefinition {
    val categories: Array<WaifuAssetCategory>
}
data class VisualMotivationAssetDefinition(
    val imagePath: String,
    val imageAlt: String,
    override val categories: Array<WaifuAssetCategory>
): AssetDefinition

data class AudibleMotivationAssetDefinition(
    val soundFile: String,
    override val categories: Array<WaifuAssetCategory>
): AssetDefinition

data class TextualMotivationAssetDefinition(
    val title: String,
    val message: String,
    override val categories: Array<WaifuAssetCategory>
): AssetDefinition
