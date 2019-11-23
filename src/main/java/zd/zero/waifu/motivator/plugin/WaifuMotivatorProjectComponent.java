package zd.zero.waifu.motivator.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertImpl;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifier;
import zd.zero.waifu.motivator.plugin.alert.notification.WaifuMotivatorNotifierImpl;
import zd.zero.waifu.motivator.plugin.alert.sound.WaifuMotivatorSoundPlayer;
import zd.zero.waifu.motivator.plugin.alert.sound.WaifuMotivatorSoundPlayerImpl;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTester;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTesterImpl;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTesterListenerImpl;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginSettings;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

public class WaifuMotivatorProjectComponent implements ProjectComponent {

    private Project project;

    private WaifuMotivatorState pluginState;

    private WaifuUnitTester unitTestListener;

    public WaifuMotivatorProjectComponent( Project project ) {
        this.project = project;
        this.pluginState = WaifuMotivatorPluginSettings.getInstance().getState();
        this.unitTestListener = new WaifuUnitTesterImpl(
                project.getMessageBus().connect(),
                new WaifuUnitTesterListenerImpl( project )
        );
    }

    @Override
    public void projectOpened() {
        unitTestListener.init();
        initializeStartupMotivator();
    }

    @Override
    public void disposeComponent() {
        this.unitTestListener.stop();
    }

    private void initializeStartupMotivator() {
        WaifuMotivatorNotifier notifier = new WaifuMotivatorNotifierImpl( project, "Mayushi", "Tuturuuu...Mayushii Desu!" );
        WaifuMotivatorSoundPlayer player = new WaifuMotivatorSoundPlayerImpl( "Tuturuu...Mayushii Desu!.wav" );

        AlertConfiguration config = AlertConfiguration.builder()
                .isAlertEnabled( pluginState.isStartupMotivationEnabled() )
                .isDisplayNotificationEnabled( true )
                .isSoundAlertEnabled( true )
                .build();
        WaifuMotivatorAlert motivatorAlert = new WaifuMotivatorAlertImpl( notifier, player, config );

        if ( !project.isInitialized() ) {
            StartupManager startupManager = StartupManager.getInstance( project );
            startupManager.registerPostStartupActivity( motivatorAlert::alert );
        } else {
            motivatorAlert.alert();
        }
    }

}
