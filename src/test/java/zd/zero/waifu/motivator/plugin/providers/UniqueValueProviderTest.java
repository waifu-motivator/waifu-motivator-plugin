package zd.zero.waifu.motivator.plugin.providers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

public class UniqueValueProviderTest {

    private static final String KEY_PROPERTY_TEST = "WMP_KEY_PROPERTY_TEST";

    private UniqueValueProvider<String> uniqueValueProvider;

    @Before
    public void setup() {
        uniqueValueProvider = new UniqueValueProviderMock( KEY_PROPERTY_TEST );
    }

    @Test
    public void Should_NotFilter_When_ValuesSizeIsOne() {
        String[] values = { "a" };
        List<String> uniqueValues = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        Assert.assertEquals( 1, uniqueValues.size() );
    }

    @Test
    public void Should_NotFilter_When_ThereAreNoValues() {
        String[] values = {};
        List<String> uniqueValues = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        Assert.assertEquals( 0, uniqueValues.size() );
    }

    @Test
    public void Should_NotFilter_When_ValuesSizeIsOneAndIfAddedToSeenValues() {
        String[] values = { "a" };
        uniqueValueProvider.addToSeenValues( "a" );
        List<String> uniqueValues = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        Assert.assertEquals( 1, uniqueValues.size() );
    }

    @Test
    public void Should_NotGetTheSeenValues() {
        String[] values = { "a", "b", "c" };

        uniqueValueProvider.addToSeenValues( "b" );
        List<String> uniqueValues = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        Assert.assertFalse( uniqueValues.contains( "b" ) );
    }

    @Test
    public void Should_NotGetTheLastSeenValues() {
        String[] values = { "a", "b" };

        uniqueValueProvider.addToSeenValues( "a" );
        List<String> uniqueValues = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        Assert.assertEquals( 1, uniqueValues.size() );

        String b = uniqueValues.get( 0 );
        uniqueValueProvider.addToSeenValues( b );
        List<String> secondCall = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        Assert.assertEquals( 1, secondCall.size() );
        Assert.assertNotEquals( b, secondCall.get( 0 ) );
    }

    @Test
    public void Should_ConsumeAllValues_And_NotRepeatTheLastSeenValue() {
        String[] values = { "a", "b", "c" };

        uniqueValueProvider.addToSeenValues( "a" );
        uniqueValueProvider.addToSeenValues( "b" );

        List<String> uniqueValues = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        String lastSeenValue = uniqueValues.get( 0 );
        Assert.assertEquals( "c", lastSeenValue );

        uniqueValueProvider.addToSeenValues( lastSeenValue );
        List<String> uniqueNoLastSeenValue = uniqueValueProvider.getUniqueValues( values, Function.identity() );
        Assert.assertFalse( uniqueNoLastSeenValue.contains( lastSeenValue ) );

    }
}
