package zd.zero.waifu.motivator.plugin.motivation;

public interface WaifuMotivation {

    boolean isDisplayNotificationEnabled();

    void displayNotification();

    boolean isSoundAlertEnabled();

    void soundAlert();

    boolean isAlertEnabled();

    boolean isDistractionAllowed();

    boolean isNotificationShowing();

    void closeNotification();

    void motivate();

    WaifuMotivation setListener( MotivationListener motivationListener);

}
