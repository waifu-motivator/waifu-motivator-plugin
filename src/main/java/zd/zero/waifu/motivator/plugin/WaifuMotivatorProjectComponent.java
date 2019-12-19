package zd.zero.waifu.motivator.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.ToolWindowManager;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertFactory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAsset;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
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
        this.unitTestListener = new WaifuUnitTesterImpl( project.getMessageBus().connect(),
                new WaifuUnitTesterListenerImpl( project ) );
    }

    @Override
    public void projectOpened() {
        unitTestListener.init();
        initializeStartupMotivator();
        registerToolWindow();
    }

    @Override
    public void disposeComponent() {
        this.unitTestListener.stop();
    }

    private void initializeStartupMotivator() {
        AlertConfiguration config = AlertConfiguration.builder()
                .isAlertEnabled( pluginState.isStartupMotivationEnabled() )
                .isDisplayNotificationEnabled( true )
                .isSoundAlertEnabled( true )
                .build();
        WaifuMotivatorAlert motivatorAlert = WaifuMotivatorAlertFactory.createAlert( project, WaifuMotivatorAsset.getRandomAsset(), config );

        if ( !project.isInitialized() ) {
            StartupManager startupManager = StartupManager.getInstance( project );
            startupManager.registerPostStartupActivity( motivatorAlert::alert );
        } else {
            motivatorAlert.alert();
        }
    }

    private void registerToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance( project );

    }

}
