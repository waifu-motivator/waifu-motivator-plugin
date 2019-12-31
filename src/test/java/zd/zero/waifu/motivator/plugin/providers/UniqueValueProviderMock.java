package zd.zero.waifu.motivator.plugin.providers;

import java.util.ArrayList;
import java.util.List;

public class UniqueValueProviderMock extends UniqueValueProvider<String> {

    private List<String> seenValues;

    public UniqueValueProviderMock( String PROPERTY_KEY ) {
        super( PROPERTY_KEY );
        seenValues = new ArrayList<>();
    }

    @Override
    void updateSourceValue( String value ) {
        seenValues.add( value );
    }

    @Override
    String getSourceValue() {
        return seenValues.stream().reduce( "", ( acc, v ) -> acc + "|" + v );
    }

}
