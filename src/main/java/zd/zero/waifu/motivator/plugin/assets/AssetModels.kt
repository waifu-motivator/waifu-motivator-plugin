package zd.zero.waifu.motivator.plugin.assets

import java.nio.file.Path
import java.util.UUID

interface AssetDefinition {
    val categories: List<WaifuAssetCategory>
    val path: String
    val groupId: UUID?
}

interface Asset {
    val groupId: UUID?
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
    override val categories: List<WaifuAssetCategory>,
    override val groupId: UUID? = null,
    val characters: List<String>? = null
) : AssetDefinition {
    fun toAsset(assetUrl: String): VisualMotivationAsset =
        VisualMotivationAsset(
            assetUrl,
            imageAlt,
            imageDimensions,
            groupId,
            characters
        )
}

data class VisualMotivationAsset(
    val filePath: String,
    val imageAlt: String,
    val imageDimensions: ImageDimension,
    override val groupId: UUID? = null,
    val characters: List<String>? = null
) : Asset

data class AudibleMotivationAssetDefinition(
    override val path: String,
    override val categories: List<WaifuAssetCategory>,
    override val groupId: UUID? = null
) : AssetDefinition

data class AudibleMotivationAsset(
    val soundFilePath: Path,
    override val groupId: UUID? = null
) : Asset

data class TextualMotivationAssetDefinition(
    override val path: String,
    override val categories: List<WaifuAssetCategory>,
    override val groupId: UUID? = null
) : AssetDefinition

/**
 * Asset Packages are are different kind of asset
 * and do not fit nicely into the "Asset" category.
 * Please forgive the unused attributes.
 */
data class TextualMotivationAssetPackage(
    val packagePath: Path,
    override val groupId: UUID? = null
) : Asset

data class TextualMotivationAsset(
    val title: String,
    override val groupId: UUID? = null
) : Asset
