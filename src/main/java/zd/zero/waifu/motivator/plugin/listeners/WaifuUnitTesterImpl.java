package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.execution.testframework.TestsUIUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class WaifuUnitTesterImpl implements WaifuUnitTester {

    private final MessageBusConnection busConnection;

    private final WaifuUnitTester.Listener listener;

    private final Debouncer debouncer;

    public WaifuUnitTesterImpl( MessageBusConnection busConnection,
                                Listener listener,
                                Debouncer debouncer ) {
        this.busConnection = busConnection;
        this.listener = listener;
        this.debouncer = debouncer;
    }

    @Override
    public void init() {
        busConnection.subscribe( Notifications.TOPIC, new Notifications() {
            @Override
            public void notify( @NotNull Notification notification ) {
                invokeListener( notification, TestsUIUtil.NOTIFICATION_GROUP.getDisplayId() );
            }
        } );
    }

    void invokeListener( Notification notification, final String NOTIFICATION_GROUP_DISPLAY_ID ) {
        if ( notification.getGroupId().equals( NOTIFICATION_GROUP_DISPLAY_ID ) ) {
            debouncer.debounce( () -> {
                if ( notification.getType() == NotificationType.ERROR ) {
                    listener.onUnitTestFailed();
                } else {
                    listener.onUnitTestPassed();
                }
            } );
        }
    }

    @Override
    public void stop() {
        this.busConnection.disconnect();
    }

}
