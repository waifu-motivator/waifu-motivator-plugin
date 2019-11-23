package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.execution.testframework.TestsUIUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.util.messages.MessageBusConnection;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class WaifuUnitTesterImplTest {

    private MessageBusConnection busConnection = Mockito.mock( MessageBusConnection.class );

    private WaifuUnitTester.Listener listener = Mockito.mock( WaifuUnitTester.Listener.class );

    @Test
    public void Should_DisplayPassedNotification_When_NotificationTypeIsNotError() {
        String displayId = TestsUIUtil.NOTIFICATION_GROUP.getDisplayId();
        Notification notification = new Notification( displayId, null, NotificationType.INFORMATION );
        WaifuUnitTesterImpl impl = new WaifuUnitTesterImpl( busConnection, listener );
        impl.invokeListener( notification );

        verify( listener, times( 1 ) ).onUnitTestPassed();
    }

    @Test
    public void Should_DisplayFailedNotification_When_NotificationTypeIsError() {
        String displayId = TestsUIUtil.NOTIFICATION_GROUP.getDisplayId();
        Notification notification = new Notification( displayId, null, NotificationType.ERROR );
        WaifuUnitTesterImpl impl = new WaifuUnitTesterImpl( busConnection, listener );
        impl.invokeListener( notification );

        verify( listener, times( 1 ) ).onUnitTestFailed();
    }

    @Test
    public void Should_NotDisplayNotification_When_NotificationGroupIsNotAboutTestRunner() {
        WaifuUnitTesterImpl impl = new WaifuUnitTesterImpl( busConnection, listener );
        Notification notification = new Notification( "SomeRandomNotificationGroup7143401368",
                null, NotificationType.INFORMATION );
        impl.invokeListener( notification );

        verify( listener, never() ).onUnitTestPassed();
    }

}
