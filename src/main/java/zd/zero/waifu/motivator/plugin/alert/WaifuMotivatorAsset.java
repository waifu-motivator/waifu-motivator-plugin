package zd.zero.waifu.motivator.plugin.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum WaifuMotivatorAsset {

    GANBATTE_ONII_CHAN(
            "Awww", "Ganbatte Onii-chan!", "ganbatte_onii_chan.wav",
            new WaifuMotivatorAssetType[]{ WaifuMotivatorAssetType.NEGATIVE }
    ),

    NICE_NICE_NICE_NICE(
            "Yay!", "Nice nice nice nice nice nice!", "nice_nice_nice_nice.wav",
            new WaifuMotivatorAssetType[]{ WaifuMotivatorAssetType.POSITIVE }
    ),

    TUTURUU_MAYUSHI_DESU(
            "Mayushi", "Tuturuuu...Mayushii Desu!", "tuturuu_mayushi_desu.wav",
            new WaifuMotivatorAssetType[]{ WaifuMotivatorAssetType.NEUTRAL, WaifuMotivatorAssetType.POSITIVE }
    );

    private final String title;

    private final String message;

    private final String value;

    private final WaifuMotivatorAssetType[] assetTypes;

    public static WaifuMotivatorAsset getRandomAsset() {
        WaifuMotivatorAsset[] values = WaifuMotivatorAsset.values();
        int randomIndex = new Random().nextInt( values.length );
        return values[randomIndex];
    }

    public static WaifuMotivatorAsset getRandomAssetByType( WaifuMotivatorAssetType type ) {
        WaifuMotivatorAsset[] values = Stream.of( WaifuMotivatorAsset.values() )
                .filter( a -> Arrays.asList( a.getAssetTypes() ).contains( type ) )
                .toArray( WaifuMotivatorAsset[]::new );
        int randomIndex = new Random().nextInt( values.length );
        return values[randomIndex];
    }
}
