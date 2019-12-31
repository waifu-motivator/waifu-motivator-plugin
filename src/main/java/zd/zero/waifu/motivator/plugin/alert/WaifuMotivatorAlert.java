package zd.zero.waifu.motivator.plugin.alert;

import com.intellij.notification.Notification;

public interface WaifuMotivatorAlert {

    boolean isDisplayNotificationEnabled();

    void displayNotification();

    boolean isSoundAlertEnabled();

    void soundAlert();

    boolean isAlertEnabled();

    void onAlertClosed( Notification notification );

    default void alert() {
        if ( isAlertEnabled() ) {
            if ( isDisplayNotificationEnabled() )
                displayNotification();

            if ( isSoundAlertEnabled() )
                soundAlert();

        }
    }


}
