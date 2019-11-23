package zd.zero.waifu.motivator.plugin.alert.notification;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.WaifuMotivator;

public interface WaifuMotivatorNotifier {

    Project getProject();

    String getTitle();

    String getContent();

    default void invokeNotify() {
        NotificationGroup notificationGroup = new NotificationGroup(
                WaifuMotivator.PLUGIN_ID, NotificationDisplayType.BALLOON, false
        );
        notificationGroup.createNotification()
                .setTitle( getTitle() )
                .setContent( getContent() )
                .notify( getProject() );
    }

}
