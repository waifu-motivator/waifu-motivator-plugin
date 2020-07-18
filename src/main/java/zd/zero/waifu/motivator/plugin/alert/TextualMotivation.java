package zd.zero.waifu.motivator.plugin.alert;

import com.intellij.notification.Notification;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifier;
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayer;

public class TextualMotivation extends BaseMotivation {

    @Getter
    private final WaifuMotivatorNotifier notifier;

    public TextualMotivation( WaifuMotivatorNotifier notifier,
                              WaifuSoundPlayer player,
                              AlertConfiguration config ) {
        super( player, config );
        this.notifier = notifier;
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
                    onAlertClosed( notification );
                }
            } );
        }
    }
}
