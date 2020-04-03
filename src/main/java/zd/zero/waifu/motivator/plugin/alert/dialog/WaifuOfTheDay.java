package zd.zero.waifu.motivator.plugin.alert.dialog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize( builder = WaifuOfTheDay.WaifuOfTheDayBuilder.class )
@Value
@Builder
public class WaifuOfTheDay {

    String name;

    String anime;

    String description;

    String image;

    String sourceUrl;

    String animeUrl;

    @JsonPOJOBuilder( withPrefix = "" )
    public static class WaifuOfTheDayBuilder {
    }
}
