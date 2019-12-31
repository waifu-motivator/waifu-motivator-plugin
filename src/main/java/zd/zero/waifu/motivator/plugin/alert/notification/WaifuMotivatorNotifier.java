package zd.zero.waifu.motivator.plugin.alert.notification;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.WaifuMotivator;

public interface WaifuMotivatorNotifier {

    Project getProject();

    String getTitle();

    String getContent();

    default Notification createNotification() {
        NotificationGroup notificationGroup = new NotificationGroup(
                WaifuMotivator.PLUGIN_NAME, NotificationDisplayType.BALLOON, false
        );

        return notificationGroup.createNotification()
                .setTitle( getTitle() )
                .setContent( getContent() );
    }

    default void invokeNotification( Notification notification ) {
        notification.notify( getProject() );
    }
}
