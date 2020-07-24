package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.util.messages.MessageBusConnection;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class WaifuUnitTesterImplTest {

    private final MessageBusConnection busConnection = Mockito.mock( MessageBusConnection.class );

    private static final String MOCK_NOTIFICATION_GROUP_ID = "MOCK_JETBRAINS_NOTIFICATION_GROUP_ID";

    private final WaifuUnitTester.Listener listener = Mockito.mock( WaifuUnitTester.Listener.class );

    @Test
    public void Should_DisplayPassedNotification_When_NotificationTypeIsNotError() {
        Notification notification = new Notification( MOCK_NOTIFICATION_GROUP_ID, null, NotificationType.INFORMATION );
        WaifuUnitTesterImpl impl = new WaifuUnitTesterImpl( busConnection, listener, Runnable::run );
        impl.invokeListener( notification, MOCK_NOTIFICATION_GROUP_ID );

        verify( listener, times( 1 ) ).onUnitTestPassed();
    }

    @Test
    public void Should_DisplayFailedNotification_When_NotificationTypeIsError() {
        Notification notification = new Notification( MOCK_NOTIFICATION_GROUP_ID, null, NotificationType.ERROR );
        WaifuUnitTesterImpl impl = new WaifuUnitTesterImpl( busConnection, listener, Runnable::run );
        impl.invokeListener( notification, MOCK_NOTIFICATION_GROUP_ID );

        verify( listener, times( 1 ) ).onUnitTestFailed();
    }

    @Test
    public void Should_NotDisplayNotification_When_NotificationGroupIsNotAboutTestRunner() {
        WaifuUnitTesterImpl impl = new WaifuUnitTesterImpl( busConnection, listener, Runnable::run );
        Notification notification = new Notification( "SomeRandomNotificationGroup7143401368",
                null, NotificationType.INFORMATION );
        impl.invokeListener( notification, MOCK_NOTIFICATION_GROUP_ID );

        verify( listener, never() ).onUnitTestPassed();
    }

}
