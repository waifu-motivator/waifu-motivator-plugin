package zd.zero.waifu.motivator.plugin.alert;

import com.intellij.notification.Notification;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifier;
import zd.zero.waifu.motivator.plugin.alert.sound.WaifuMotivatorSoundPlayer;

public class WaifuMotivatorAlertImpl implements WaifuMotivatorAlert {

    @Getter
    private WaifuMotivatorNotifier notifier;

    @Getter
    private WaifuMotivatorSoundPlayer player;

    @Getter
    private AlertConfiguration config;

    public WaifuMotivatorAlertImpl( WaifuMotivatorNotifier notifier, WaifuMotivatorSoundPlayer player, AlertConfiguration config ) {
        this.notifier = notifier;
        this.player = player;
        this.config = config;
    }

    @Override
    public boolean isAlertEnabled() {
        return config.isAlertEnabled();
    }

    @Override
    public void onAlertClosed() {
        player.stop();
    }

    @Override
    public void displayNotification() {
        Notification notification = notifier.createNotification();
        notifier.invokeNotification( notification );

        Balloon balloon = notification.getBalloon();
        if ( balloon != null ) {
            balloon.addListener( new JBPopupListener() {
                @Override
                public void onClosed( @NotNull LightweightWindowEvent event ) {
                    onAlertClosed();
                }
            } );
        }
    }

    @Override
    public boolean isDisplayNotificationEnabled() {
        return config.isDisplayNotificationEnabled();
    }

    @Override
    public void soundAlert() {
        player.play();
    }

    @Override
    public boolean isSoundAlertEnabled() {
        return config.isSoundAlertEnabled();
    }

}
