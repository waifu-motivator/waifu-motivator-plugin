package zd.zero.waifu.motivator.plugin.alert;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.ResourceUtil;
import org.jetbrains.annotations.NonNls;
import zd.zero.waifu.motivator.plugin.providers.UniqueValueProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class AlertAssetProvider {

    private static final String USED_ALERTS = "WMP_USED_ALERTS";

    private static WaifuMotivatorAlertAsset[] alertAssets;

    private static UniqueValueProvider<WaifuMotivatorAlertAsset> uniqueProvider;

    private static Random random;

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

        if ( uniqueProvider == null ) {
            uniqueProvider = new UniqueValueProvider<>( USED_ALERTS, assets );
        }
        WaifuMotivatorAlertAsset[] filteredAlerts = uniqueProvider.getUniqueValues( WaifuMotivatorAlertAsset::getTitle ).toArray( new WaifuMotivatorAlertAsset[0] );

        WaifuMotivatorAlertAsset asset;
        if ( filteredAlerts.length == 1 ) {
            asset = filteredAlerts[0];
        } else {
            random = random == null ? new Random() : random;
            int randomIndex = random.nextInt( filteredAlerts.length );
            asset = filteredAlerts[randomIndex];
        }
        uniqueProvider.addToSeenValues( asset.getTitle() );

        return asset;
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
