package zd.zero.waifu.motivator.plugin.assets

import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory
import java.nio.file.Path

data class MotivationAsset(
    val title: String,
    val message: String,
    val soundFilePath: Path,
    val categories: Array<WaifuMotivatorAlertAssetCategory>
)
