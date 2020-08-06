package zd.zero.waifu.motivator.plugin.assets

interface AssetDefinition {
    val categories: Array<WaifuAssetCategory>
}

data class ImageDimension(
    val width: Int,
    val height: Int
)

data class VisualMotivationAssetDefinition(
    val imagePath: String,
    val imageAlt: String,
    val imageDimensions: ImageDimension,
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition

data class AudibleMotivationAssetDefinition(
    val soundFile: String,
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition

data class TextualMotivationAssetDefinition(
    val title: String,
    val message: String,
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition
