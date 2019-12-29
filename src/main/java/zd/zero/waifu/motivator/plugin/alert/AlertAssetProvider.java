package zd.zero.waifu.motivator.plugin.alert;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.ResourceUtil;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class AlertAssetProvider {

    private static WaifuMotivatorAlertAsset[] alertAssets;

    private AlertAssetProvider() {
        throw new AssertionError( "Never instantiate." );
    }

    public static WaifuMotivatorAlertAsset getRandomAssetByCategory(
            @NonNls final WaifuMotivatorAlertAssetCategory category ) {

        WaifuMotivatorAlertAsset[] assets = getWaifuMotivatorAlertAssets();
        if ( !category.equals( WaifuMotivatorAlertAssetCategory.ALL ) ) {
            assets = Stream.of( assets )
                    .filter( a -> Arrays.asList( a.getCategories() ).contains( category ) )
                    .toArray( WaifuMotivatorAlertAsset[]::new );
        }

        int randomIndex = new Random().nextInt( assets.length );
        return assets[randomIndex];
    }

    private static WaifuMotivatorAlertAsset[] getWaifuMotivatorAlertAssets() {
        if ( alertAssets == null ) {
            ClassLoader classLoader = AlertAssetProvider.class.getClassLoader();
            try ( InputStream resource = ResourceUtil.getResourceAsStream( classLoader,
                    "/", "waifu_alerts.json" ) ) {
                if ( resource == null ) {
                    throw new IOException( "Cannot find the waifu alerts." );
                }

                ObjectMapper mapper = new ObjectMapper();
                mapper.enable( MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS );

                alertAssets = mapper.readValue( resource, WaifuMotivatorAlertAsset[].class );
            } catch ( IOException e ) {
                alertAssets = new WaifuMotivatorAlertAsset[0];
            }
        }

        return alertAssets;
    }
}
