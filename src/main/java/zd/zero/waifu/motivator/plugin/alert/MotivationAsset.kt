package zd.zero.waifu.motivator.plugin.alert

import com.google.gson.annotations.SerializedName

data class MotivationAsset(
    val title: String,
    val message: String,
    @SerializedName("sound")
    val soundFileName: String,
    val categories: Array<WaifuMotivatorAlertAssetCategory>
)
