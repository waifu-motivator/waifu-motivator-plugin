package zd.zero.waifu.motivator.plugin.alert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize( builder = WaifuMotivatorAlertAsset.WaifuMotivatorAlertAssetBuilder.class )
@Value
@Builder
public class WaifuMotivatorAlertAsset {

    private final String title;

    private final String message;

    private final String sound;

    private final WaifuMotivatorAlertAssetCategory[] categories;

    @JsonPOJOBuilder( withPrefix = "" )
    public static class WaifuMotivatorAlertAssetBuilder {

    }
}
