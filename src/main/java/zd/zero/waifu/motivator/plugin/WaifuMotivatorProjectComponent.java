package zd.zero.waifu.motivator.plugin;

import com.intellij.ide.GeneralSettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.startup.StartupManager;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertFactory;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.sound.DefaultWaifuMotivatorSoundPlayer;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTester;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.concurrent.ThreadLocalRandom;

public class WaifuMotivatorProjectComponent implements ProjectComponent, Disposable {

    private static final String IS_INITIAL_PLATFORM_TIP_UPDATED = "WAIFU_UPDATE_TIP";

    private Project project;

    private WaifuMotivatorState pluginState;

    private ApplicationListener applicationListener;

    private WaifuUnitTester unitTestListener;

    public WaifuMotivatorProjectComponent( Project project ) {
        this.project = project;
        this.pluginState = WaifuMotivatorPluginState.getPluginState();
        this.unitTestListener = WaifuUnitTester.ofDefault( project );
    }

    @Override
    public void projectOpened() {
        updatePlatformStartupConfig();
        initializeListeners();
        initializeStartupMotivator();
    }

    @Override
    public void dispose() {
        this.unitTestListener.stop();
        if ( pluginState.isSayonaraEnabled() ) {
            applicationListener.applicationExiting();
        }
    }

    private void initializeListeners() {
        unitTestListener.init();

        this.applicationListener = new ApplicationListener() {
            @Override
            public void applicationExiting() {
                Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
                if ( openProjects.length > 0 ) return;

                final String[] sayonara = { "ara_ara_sayonara.wav", "sayonara_bye_bye.wav", "sayonara_senpai.wav" };
                DefaultWaifuMotivatorSoundPlayer
                        .ofFile( sayonara[ThreadLocalRandom.current().nextInt( sayonara.length )] )
                        .playAndWait();
            }
        };
        ApplicationManager.getApplication().addApplicationListener( applicationListener, this );
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
            StartupManager.getInstance( project ).registerPostStartupActivity( motivatorAlert::alert );
        } else {
            motivatorAlert.alert();
        }
    }

}
