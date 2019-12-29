package zd.zero.waifu.motivator.plugin.alert.dialog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize( builder = WaifuOfTheDay.WaifuOfTheDayBuilder.class )
@Value
@Builder
public class WaifuOfTheDay {

    private String name;

    private String anime;

    private String description;

    private String image;

    private String sourceUrl;

    private String animeUrl;

    @JsonPOJOBuilder( withPrefix = "" )
    public static class WaifuOfTheDayBuilder {
    }
}
