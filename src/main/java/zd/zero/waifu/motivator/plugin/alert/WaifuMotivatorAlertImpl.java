package zd.zero.waifu.motivator.plugin.alert;

import lombok.Getter;
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
    public void displayNotification() {
        notifier.invokeNotify();
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
