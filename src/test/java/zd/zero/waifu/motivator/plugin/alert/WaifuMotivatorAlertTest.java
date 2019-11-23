package zd.zero.waifu.motivator.plugin.alert;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class WaifuMotivatorAlertTest {

    private WaifuMotivatorAlert motivatorAlert = mock( WaifuMotivatorAlert.class );

    @Before
    public void setup() {
        doCallRealMethod().when( motivatorAlert ).alert();
        doNothing().when( motivatorAlert ).displayNotification();
        doNothing().when( motivatorAlert ).soundAlert();

        when( motivatorAlert.isAlertEnabled() ).thenReturn( false );
        when( motivatorAlert.isDisplayNotificationEnabled() ).thenReturn( false );
        when( motivatorAlert.isSoundAlertEnabled() ).thenReturn( false );
    }

    @Test
    public void Should_NotDisplayAlert_When_AlertIsDisabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( false );
        motivatorAlert.alert();

        verify( motivatorAlert, never() ).displayNotification();
    }

    @Test
    public void Should_DisplayAlert_When_AllAlertsAreEnabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( true );
        when( motivatorAlert.isDisplayNotificationEnabled() ).thenReturn( true );
        when( motivatorAlert.isSoundAlertEnabled() ).thenReturn( true );
        motivatorAlert.alert();

        verify( motivatorAlert, times( 1 ) ).displayNotification();
    }

    @Test
    public void Should_SoundAlert_When_AllAlertsAreEnabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( true );
        when( motivatorAlert.isDisplayNotificationEnabled() ).thenReturn( true );
        when( motivatorAlert.isSoundAlertEnabled() ).thenReturn( true );
        motivatorAlert.alert();

        verify( motivatorAlert, times( 1 ) ).soundAlert();
    }

    @Test
    public void Should_NotDisplayAlert_WhenNotificationIsDisabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( true );
        when( motivatorAlert.isDisplayNotificationEnabled() ).thenReturn( false );
        motivatorAlert.alert();

        verify( motivatorAlert, never() ).displayNotification();
    }

    @Test
    public void Should_NotSoundAlert_WhenSoundIsDisabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( true );
        when( motivatorAlert.isSoundAlertEnabled() ).thenReturn( false );
        motivatorAlert.alert();

        verify( motivatorAlert, never() ).soundAlert();
    }

    @Test
    public void Should_DisplayAlert_WhenNotificationIsEnabledAndSoundIsDisabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( true );
        when( motivatorAlert.isDisplayNotificationEnabled() ).thenReturn( true );
        when( motivatorAlert.isSoundAlertEnabled() ).thenReturn( false );
        motivatorAlert.alert();

        verify( motivatorAlert, times( 1 ) ).displayNotification();
    }

    @Test
    public void Should_SoundAlert_WhenSoundIsEnabledAndNotificationIsDisabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( true );
        when( motivatorAlert.isDisplayNotificationEnabled() ).thenReturn( false );
        when( motivatorAlert.isSoundAlertEnabled() ).thenReturn( true );
        motivatorAlert.alert();

        verify( motivatorAlert, times( 1 ) ).soundAlert();
    }

    @Test
    public void Should_NotDisplayAlert_WhenAlertIsEnabledButNotificationIsDisabled() {
        when( motivatorAlert.isAlertEnabled() ).thenReturn( true );
        motivatorAlert.alert();

        verify( motivatorAlert, never() ).displayNotification();
    }

}
