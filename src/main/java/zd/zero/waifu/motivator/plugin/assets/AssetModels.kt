package zd.zero.waifu.motivator.plugin.assets

import java.nio.file.Path

interface AssetDefinition {
    val categories: List<WaifuAssetCategory>
    val path: String
}

interface Asset

data class ImageDimension(
    val width: Int,
    val height: Int
)

data class VisualMotivationAssetDefinition(
    val imagePath: String, // todo: remove this once migrated
    override val path: String,
    val imageAlt: String,
    val imageDimensions: ImageDimension,
    override val categories: List<WaifuAssetCategory>
) : AssetDefinition

data class VisualMotivationAsset(
    val filePath: String,
    val imageAlt: String,
    val imageDimensions: ImageDimension
) : Asset

data class AudibleMotivationAssetDefinition(
    override val path: String,
    override val categories: List<WaifuAssetCategory>
) : AssetDefinition

data class AudibleMotivationAsset(
    val soundFilePath: Path
) : Asset

data class TextualMotivationAssetDefinition(
    override val path: String,
    override val categories: List<WaifuAssetCategory>
) : AssetDefinition

data class TextualMotivationAssetPackage(
    val packagePath: Path
) : Asset

data class TextualMotivationAsset(
    val title: String
)
