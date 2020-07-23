package zd.zero.waifu.motivator.plugin.alert

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WaifuMotivatorAlertAssetCategoryDeserializer : JsonDeserializer<WaifuMotivatorAlertAssetCategory> {
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): WaifuMotivatorAlertAssetCategory =
        WaifuMotivatorAlertAssetCategory.valueOf(jsonElement.asString.toUpperCase())
}
