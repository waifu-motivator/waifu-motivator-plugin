package zd.zero.waifu.motivator.plugin.assets

import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths

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
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition {
    val soundFilePath: Path
    get() = Paths.get(URI(path))
}

data class TextualMotivationAssetDefinition(
    override val path: String,
    override val categories: Array<WaifuAssetCategory>
) : AssetDefinition

data class TextualMotivationAsset(
    val title: String
)
