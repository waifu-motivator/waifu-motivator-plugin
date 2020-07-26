package zd.zero.waifu.motivator.plugin.alert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.ResourceUtil;
import org.jetbrains.annotations.NonNls;
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset;
import zd.zero.waifu.motivator.plugin.providers.UniqueValueProvider;
import zd.zero.waifu.motivator.plugin.tools.WaifuMotivatorAlertAssetCategoryDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class AlertAssetProvider {

    private static final Logger LOGGER = Logger.getInstance( AlertAssetProvider.class );

    private static final String USED_ALERTS = "WMP_USED_ALERTS";

    private static MotivationAsset[] alertAssets;

    private static UniqueValueProvider<MotivationAsset> uniqueProvider;

    private static Random random;

    private AlertAssetProvider() {
        throw new AssertionError( "Never instantiate." );
    }

    public static MotivationAsset getRandomAssetByCategory(
            @NonNls final WaifuMotivatorAlertAssetCategory category ) {

        MotivationAsset[] assets = getWaifuMotivatorAlertAssets();
        if ( !category.equals( WaifuMotivatorAlertAssetCategory.ALL ) ) {
            assets = Stream.of( assets )
                    .filter( a -> Arrays.asList( a.getCategories() ).contains( category ) )
                    .toArray( MotivationAsset[]::new );
        }

        if ( uniqueProvider == null ) {
            uniqueProvider = new UniqueValueProvider<>( USED_ALERTS );
        }
        MotivationAsset[] filteredAlerts = uniqueProvider
                .getUniqueValues( assets, MotivationAsset::getTitle )
                .toArray( new MotivationAsset[0] );

        MotivationAsset asset;
        if ( filteredAlerts.length == 1 ) {
            asset = filteredAlerts[0];
        } else {
            random = random == null ? ThreadLocalRandom.current() : random;
            int randomIndex = random.nextInt( filteredAlerts.length );
            asset = filteredAlerts[randomIndex];
        }
        uniqueProvider.addToSeenValues( asset.getTitle() );

        return asset;
    }

    private static MotivationAsset[] getWaifuMotivatorAlertAssets() {
        if ( alertAssets == null ) {
            ClassLoader classLoader = AlertAssetProvider.class.getClassLoader();
            try ( InputStream resource = ResourceUtil.getResourceAsStream( classLoader,
                    "/", "waifu_alerts.json" ) ) {
                if ( resource == null ) {
                    throw new IOException( "Cannot find the waifu alerts." );
                }

                Gson gson = new GsonBuilder().registerTypeAdapter(
                    WaifuMotivatorAlertAssetCategory.class,
                    new WaifuMotivatorAlertAssetCategoryDeserializer()
                ).create();

                alertAssets = gson.fromJson( new InputStreamReader( resource ), MotivationAsset[].class );
            } catch ( IOException e ) {
                alertAssets = new MotivationAsset[0];
                LOGGER.error( "Unable to parse sound source file.", e );
            }
        }

        return alertAssets;
    }
}
