package zd.zero.waifu.motivator.plugin;

import com.intellij.ide.GeneralSettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertFactory;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTester;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

public class WaifuMotivatorProjectComponent implements ProjectComponent {

    private static final String IS_INITIAL_PLATFORM_TIP_UPDATED = "WAIFU_UPDATE_TIP";

    private Project project;

    private WaifuMotivatorState pluginState;

    private WaifuUnitTester unitTestListener;

    public WaifuMotivatorProjectComponent( Project project ) {
        this.project = project;
        this.pluginState = WaifuMotivatorPluginState.getPluginState();
        this.unitTestListener = WaifuUnitTester.ofDefault( project );
    }

    @Override
    public void projectOpened() {
        unitTestListener.init();
        updatePlatformStartupConfig();
        initializeStartupMotivator();
    }

    @Override
    public void disposeComponent() {
        this.unitTestListener.stop();
    }

    private void updatePlatformStartupConfig() {
        boolean isInitialPlatformTipUpdated = Boolean.parseBoolean( PropertiesComponent.getInstance()
                .getValue( IS_INITIAL_PLATFORM_TIP_UPDATED, "" ) );
        if ( !isInitialPlatformTipUpdated ) {
            PropertiesComponent.getInstance().setValue( IS_INITIAL_PLATFORM_TIP_UPDATED, true );
            GeneralSettings.getInstance().setShowTipsOnStartup( !pluginState.isWaifuOfTheDayEnabled() );
        }
    }

    private void initializeStartupMotivator() {
        AlertConfiguration config = AlertConfiguration.builder()
                .isAlertEnabled( pluginState.isStartupMotivationEnabled() )
                .isDisplayNotificationEnabled( true )
                .isSoundAlertEnabled( true )
                .build();
        WaifuMotivatorAlert motivatorAlert = WaifuMotivatorAlertFactory.createAlert(
                project, AlertAssetProvider.getRandomAssetByCategory( WaifuMotivatorAlertAssetCategory.NEUTRAL ), config );

        if ( !project.isInitialized() ) {
            StartupManager startupManager = StartupManager.getInstance( project );
            startupManager.registerPostStartupActivity( motivatorAlert::alert );
        } else {
            motivatorAlert.alert();
        }
    }

}
