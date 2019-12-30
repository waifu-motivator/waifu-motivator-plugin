package zd.zero.waifu.motivator.plugin.providers;

import com.intellij.ide.util.PropertiesComponent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class UniqueValueProvider<T> {

    private final String PROPERTY_KEY;

    private T[] initialValues;

    private String lastSeenValue = "";

    public UniqueValueProvider( final String PROPERTY_KEY, T[] initialValues ) {
        this.PROPERTY_KEY = PROPERTY_KEY;
        this.initialValues = initialValues;
    }

    public List<T> getUniqueValues( Function<T, String> function ) {
        String[] usedKeyValues = getValue().split( "\\|" );

        List<T> filteredValues = Arrays.stream( initialValues )
                .filter( v -> !Arrays.asList( usedKeyValues ).contains( function.apply( v ) ) ).collect( toList() );

        if ( filteredValues.isEmpty() ) {
            filteredValues = Arrays.stream( initialValues )
                    .filter( v -> !function.apply( v ).equals( lastSeenValue ) ).collect( toList() );
            PropertiesComponent.getInstance().setValue( PROPERTY_KEY, "" );
        }

        return filteredValues;
    }

    public void addToSeenValues( String seenValue ) {
        this.lastSeenValue = seenValue;
        PropertiesComponent.getInstance().setValue( PROPERTY_KEY, getValue() + "|" + seenValue );
    }

    private String getValue() {
        return PropertiesComponent.getInstance().getValue( PROPERTY_KEY, "" );
    }
}
