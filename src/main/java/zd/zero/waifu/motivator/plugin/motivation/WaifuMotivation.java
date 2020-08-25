package zd.zero.waifu.motivator.plugin.motivation;

import com.intellij.notification.Notification;

public interface WaifuMotivation {

    boolean isDisplayNotificationEnabled();

    void displayNotification();

    boolean isSoundAlertEnabled();

    void soundAlert();

    boolean isAlertEnabled();

    boolean isDistractionAllowed();

    void onAlertClosed( Notification notification );

    void motivate();

    WaifuMotivation setListener( MotivationListener motivationListener);
}
