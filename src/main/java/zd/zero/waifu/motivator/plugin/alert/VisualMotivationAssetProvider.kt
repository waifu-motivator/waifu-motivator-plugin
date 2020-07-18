package zd.zero.waifu.motivator.plugin.alert

import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory.POSITIVE

object VisualMotivationAssetProvider: AssetProvider {

    override fun pickRandomByCategory(category: WaifuMotivatorAlertAssetCategory): MotivationAsset {
        return when(category) {
            POSITIVE -> MotivationAsset(
                "Title",
                """
                    
                """.trimIndent(),
                "",
                arrayOf()
            )
            else -> throw NotImplementedError("You can't use $category here.")
        }
    }
}


