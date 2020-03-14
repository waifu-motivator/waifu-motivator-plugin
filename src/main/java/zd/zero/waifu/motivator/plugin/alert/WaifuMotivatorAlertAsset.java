package zd.zero.waifu.motivator.plugin.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize( builder = WaifuMotivatorAlertAsset.WaifuMotivatorAlertAssetBuilder.class )
@Value
@Builder
public class WaifuMotivatorAlertAsset {

    String title;

    String message;

    @JsonProperty( "sound" )
    String soundFileName;

    WaifuMotivatorAlertAssetCategory[] categories;

    @JsonPOJOBuilder( withPrefix = "" )
    public static class WaifuMotivatorAlertAssetBuilder {

    }
}
