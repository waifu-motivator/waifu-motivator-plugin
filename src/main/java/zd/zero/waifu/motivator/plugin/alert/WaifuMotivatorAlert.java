package zd.zero.waifu.motivator.plugin.alert;

public interface WaifuMotivatorAlert {

    boolean isDisplayNotificationEnabled();

    void displayNotification();

    boolean isSoundAlertEnabled();

    void soundAlert();

    boolean isAlertEnabled();

    void onAlertClosed();

    default void alert() {
        if ( isAlertEnabled() ) {
            if ( isDisplayNotificationEnabled() )
                displayNotification();

            if ( isSoundAlertEnabled() )
                soundAlert();

        }
    }


}
