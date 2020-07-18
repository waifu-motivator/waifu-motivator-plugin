package zd.zero.waifu.motivator.plugin.alert

import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory.POSITIVE

object VisualMotivationAssetProvider: AssetProvider {

    override fun pickRandomByCategory(category: WaifuMotivatorAlertAssetCategory): MotivationAsset {
        return when(category) {
            POSITIVE -> MotivationAsset(
                "Title",
                """
                 <img src='https://media.tenor.com/images/bec891420a83ef648656a290e8c52e63/tenor.gif' width='256' />
                 <br>
                 <br>
                 <br>
                 <br>
                 <br>
                 <br>
                 <br>
                 <br>
                 <p style='opacity: 0'>aoeu</p>
                """.trimIndent(),
                "waoow.mp3",
                arrayOf()
            )
            else -> throw NotImplementedError("You can't use $category here.")
        }
    }
}


