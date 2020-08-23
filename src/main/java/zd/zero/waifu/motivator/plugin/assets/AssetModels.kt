package zd.zero.waifu.motivator.plugin.assets

interface AssetDefinition {
    val categories: Array<WaifuAssetCategory>
    val path: String
}

data class ImageDimension(
    val width: Int,
    val height: Int
)

data class VisualMotivationAssetDefinition(
    val imagePath: String, // todo: remove this once migrated
    override val path: String,
    val imageAlt: String,
    val imageDimensions: ImageDimension,
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition

data class AudibleMotivationAssetDefinition(
    override val path: String,
    val soundFile: String,
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition

data class TextualMotivationAssetDefinition(
    override val path: String,
    val title: String,
    val message: String,
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition
