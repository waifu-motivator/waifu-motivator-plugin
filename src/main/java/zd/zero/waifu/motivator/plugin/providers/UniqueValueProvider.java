package zd.zero.waifu.motivator.plugin.providers;

import com.intellij.ide.util.PropertiesComponent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class UniqueValueProvider<T> {

    private final String PROPERTY_KEY;

    private String lastSeenValue = "";

    public UniqueValueProvider( final String PROPERTY_KEY ) {
        this.PROPERTY_KEY = PROPERTY_KEY;
    }

    public List<T> getUniqueValues( T[] initialValues, Function<T, String> function ) {
        if ( initialValues.length <= 1 ) {
            return Arrays.asList( initialValues );
        }

        String[] usedKeyValues = getSourceValue().split( "\\|" );

        List<T> filteredValues = Arrays.stream( initialValues )
                .filter( v -> !Arrays.asList( usedKeyValues ).contains( function.apply( v ) ) ).collect( toList() );

        if ( filteredValues.isEmpty() ) {
            filteredValues = Arrays.stream( initialValues )
                    .filter( v -> !function.apply( v ).equals( lastSeenValue ) ).collect( toList() );
            updateSourceValue( "" );
        }

        return filteredValues;
    }

    public void addToSeenValues( String seenValue ) {
        this.lastSeenValue = seenValue;
        updateSourceValue( getSourceValue() + "|" + seenValue );
    }

    void updateSourceValue( String value ) {
        PropertiesComponent.getInstance().setValue( PROPERTY_KEY, value );
    }

    String getSourceValue() {
        return PropertiesComponent.getInstance().getValue( PROPERTY_KEY, "" );
    }
}
