package zd.zero.waifu.motivator.plugin;

import com.intellij.ide.GeneralSettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.startup.StartupManager;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertFactory;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTester;
import zd.zero.waifu.motivator.plugin.onboarding.UserOnboarding;
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.concurrent.ThreadLocalRandom;

import static zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory.NEUTRAL;

public class WaifuMotivatorProject implements ProjectManagerListener, Disposable {

    private static final String IS_INITIAL_PLATFORM_TIP_UPDATED = "WAIFU_UPDATE_TIP";

    private Project project;

    private WaifuMotivatorState pluginState;

    private WaifuUnitTester unitTestListener;

    @Override
    public void projectOpened( @NotNull Project projectOpened ) {
        if ( this.project != null ) return;

        this.project = projectOpened;
        this.pluginState = WaifuMotivatorPluginState.getPluginState();
        this.unitTestListener = WaifuUnitTester.newInstance( projectOpened );

        updatePlatformStartupConfig();
        initializeListeners();
        initializeStartupMotivator();
        UserOnboarding.INSTANCE.attemptToShowUpdateNotification();
    }

    @Override
    public void projectClosing( @NotNull Project project ) {
        if ( !pluginState.isSayonaraEnabled() || isMultipleProjectsOpened() ) return;

        final String[] sayonara = { "ara_ara_sayonara.wav", "sayonara_bye_bye.wav", "sayonara_senpai.wav" };
        String file = sayonara[ThreadLocalRandom.current().nextInt( sayonara.length )];
        WaifuSoundPlayerFactory.createPlayer( file ).playAndWait();
    }

    @Override
    public void dispose() {
        this.unitTestListener.stop();
    }

    private void initializeListeners() {
        this.unitTestListener.init();
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
        if ( isMultipleProjectsOpened() ) return;

        AlertConfiguration config = AlertConfiguration.builder()
            .isAlertEnabled( pluginState.isStartupMotivationEnabled() || pluginState.isStartupMotivationSoundEnabled() )
            .isDisplayNotificationEnabled( pluginState.isStartupMotivationEnabled() )
            .isSoundAlertEnabled( pluginState.isStartupMotivationSoundEnabled() )
            .build();
        WaifuMotivatorAlert motivatorAlert = WaifuMotivatorAlertFactory
            .createAlert(
                project,
                AlertAssetProvider.getRandomAssetByCategory( NEUTRAL ),
                config
            );

        if ( !project.isInitialized() ) {
            StartupManager.getInstance( project ).registerPostStartupActivity( motivatorAlert::alert );
        } else {
            motivatorAlert.alert();
        }
    }

    private boolean isMultipleProjectsOpened() {
        return ProjectManager.getInstance().getOpenProjects().length > 1;
    }

}
