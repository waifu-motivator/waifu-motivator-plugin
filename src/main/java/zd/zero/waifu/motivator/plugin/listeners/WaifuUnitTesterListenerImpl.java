package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertImpl;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifier;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifierImpl;
import zd.zero.waifu.motivator.plugin.alert.sound.WaifuMotivatorSoundPlayer;
import zd.zero.waifu.motivator.plugin.alert.sound.WaifuMotivatorSoundPlayerImpl;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginSettings;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.Objects;


public class WaifuUnitTesterListenerImpl implements WaifuUnitTester.Listener {

    private final Project project;

    private WaifuMotivatorState pluginState;

    public WaifuUnitTesterListenerImpl( Project project ) {
        this.project = project;
        this.pluginState = WaifuMotivatorPluginSettings.getInstance().getState();
    }

    @Override
    public void onUnitTestPassed() {
        WaifuMotivatorNotifier notifier = new WaifuMotivatorNotifierImpl( project, "Yay!", "Nice nice nice nice nice nice!" );
        WaifuMotivatorSoundPlayer player = new WaifuMotivatorSoundPlayerImpl( "nice nice nice nice.wav" );
        WaifuMotivatorAlertImpl passedAlert = new WaifuMotivatorAlertImpl( notifier, player, getUnitTesterConfiguration() );
        passedAlert.alert();
    }

    @Override
    public void onUnitTestFailed() {
        WaifuMotivatorNotifier notifier = new WaifuMotivatorNotifierImpl( project, "Awww", "Ganbatte Onii-chan!" );
        WaifuMotivatorSoundPlayer player = new WaifuMotivatorSoundPlayerImpl( "Ganbatte Onii-chan!.wav" );
        WaifuMotivatorAlertImpl failedAlert = new WaifuMotivatorAlertImpl( notifier, player, getUnitTesterConfiguration() );
        failedAlert.alert();
    }

    private AlertConfiguration getUnitTesterConfiguration() {
        return AlertConfiguration.builder()
                .isAlertEnabled( Objects.requireNonNull( pluginState ).isUnitTesterMotivationEnabled() )
                .isDisplayNotificationEnabled( true )
                .isSoundAlertEnabled( true )
                .build();
    }
}
