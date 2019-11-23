package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertImpl;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifier;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifierImpl;
import zd.zero.waifu.motivator.plugin.alert.sound.WaifuMotivatorSoundPlayer;
import zd.zero.waifu.motivator.plugin.alert.sound.WaifuMotivatorSoundPlayerImpl;

public class MotivateMeAction extends AnAction {

    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        WaifuMotivatorNotifier notifier = new WaifuMotivatorNotifierImpl( e.getProject(), "Mayushi", "Tuturuuu...Mayushii Desu!" );
        WaifuMotivatorSoundPlayer player = new WaifuMotivatorSoundPlayerImpl( "Tuturuu...Mayushii Desu!.wav" );

        AlertConfiguration config = AlertConfiguration.builder()
                .isAlertEnabled( true )
                .isDisplayNotificationEnabled( true )
                .isSoundAlertEnabled( true )
                .build();
        WaifuMotivatorAlert motivatorAlert = new WaifuMotivatorAlertImpl( notifier, player, config );
        motivatorAlert.alert();
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
