package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.execution.testframework.TestsUIUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.notification.NotificationsAdapter;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class WaifuUnitTesterImpl implements WaifuUnitTester {

    private final MessageBusConnection busConnection;

    private WaifuUnitTester.Listener listener;

    public WaifuUnitTesterImpl( MessageBusConnection busConnection, WaifuUnitTester.Listener listener ) {
        this.busConnection = busConnection;
        this.listener = listener;
    }

    @Override
    public void init() {
        busConnection.subscribe( Notifications.TOPIC, new NotificationsAdapter() {
            @Override
            public void notify( @NotNull Notification notification ) {
                invokeListener( notification );
            }
        } );
    }

    void invokeListener( Notification notification ) {
        if ( notification.getGroupId().equals( TestsUIUtil.NOTIFICATION_GROUP.getDisplayId() ) ) {
            if ( notification.getType() == NotificationType.ERROR ) {
                listener.onUnitTestFailed();
            } else {
                listener.onUnitTestPassed();
            }
        }
    }

    @Override
    public void stop() {
        this.busConnection.disconnect();
    }

}
