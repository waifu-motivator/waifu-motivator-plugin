package zd.zero.waifu.motivator.plugin.assets

import com.google.gson.annotations.SerializedName
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory

data class MotivationAsset(
    val title: String,
    val message: String,
    @SerializedName("sound")
    val soundFileName: String,
    val categories: Array<WaifuMotivatorAlertAssetCategory>
)
