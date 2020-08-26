package zd.zero.waifu.motivator.plugin;

import com.intellij.ide.GeneralSettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.startup.StartupManager;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.assets.AudibleAssetDefinitionService;
import zd.zero.waifu.motivator.plugin.assets.AudibleAssetManager;
import zd.zero.waifu.motivator.plugin.assets.HasStatus;
import zd.zero.waifu.motivator.plugin.assets.Status;
import zd.zero.waifu.motivator.plugin.assets.TextAssetManager;
import zd.zero.waifu.motivator.plugin.assets.VisualAssetManager;
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider;
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory;
import zd.zero.waifu.motivator.plugin.listeners.IdleEventListener;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTester;
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory;
import zd.zero.waifu.motivator.plugin.motivation.WaifuMotivation;
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification;
import zd.zero.waifu.motivator.plugin.onboarding.UserOnboarding;
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static zd.zero.waifu.motivator.plugin.tools.ToolBox.doOrElse;

public class WaifuMotivatorProject implements ProjectManagerListener, Disposable {

    private static final String IS_INITIAL_PLATFORM_TIP_UPDATED = "WAIFU_UPDATE_TIP";

    private Project project;

    private WaifuMotivatorState pluginState;

    private WaifuUnitTester unitTestListener;

    private IdleEventListener idleEventListener;

    @Override
    public void projectOpened( @NotNull Project projectOpened ) {
        checkIfInGoodState(projectOpened);
        if ( this.project != null ) return;

        this.project = projectOpened;
        this.pluginState = WaifuMotivatorPluginState.getPluginState();
        this.unitTestListener = WaifuUnitTester.newInstance( projectOpened );
        this.idleEventListener = new IdleEventListener();

        updatePlatformStartupConfig();
        initializeListeners();
        initializeStartupMotivator();
        UserOnboarding.INSTANCE.attemptToShowUpdateNotification();
    }

    private void checkIfInGoodState( Project projectOpened ) {
        StartupManager.getInstance( projectOpened ).registerPostStartupActivity( () -> {
            boolean isInGoodState = Stream.of(
                TextAssetManager.INSTANCE,
                VisualAssetManager.INSTANCE,
                AudibleAssetManager.INSTANCE
            ).map( HasStatus::getStatus )
                .allMatch( Predicate.isEqual( Status.OK ) );
            if ( !isInGoodState ) {
                // todo: add help link?
                UpdateNotification.INSTANCE.sendMessage(
                    "Unable setup correctly!",
                    "The plugin requires internet first before offline mode can work.",
                    projectOpened
                );
            }
        } );
    }

    @Override
    public void projectClosing( @NotNull Project project ) {
        if ( !pluginState.isSayonaraEnabled() || isMultipleProjectsOpened() ) return;

        AudibleAssetDefinitionService.INSTANCE.getRandomAssetByCategory(
            WaifuAssetCategory.DEPARTURE
        ).ifPresent( asset -> WaifuSoundPlayerFactory.createPlayer( asset.getSoundFilePath() ).playAndWait() );
    }

    @Override
    public void dispose() {
        this.unitTestListener.stop();
        this.idleEventListener.dispose();
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
        if ( isMultipleProjectsOpened()
            || UserOnboarding.INSTANCE.isNewVersion() ) return;

        AlertConfiguration config = new AlertConfiguration(
            pluginState.isStartupMotivationEnabled() || pluginState.isStartupMotivationSoundEnabled(),
            pluginState.isStartupMotivationEnabled(),
            pluginState.isStartupMotivationSoundEnabled()
        );

        // todo: replace with presentOrElse when only supporting JRE 11+
        doOrElse( VisualMotivationAssetProvider.INSTANCE.createAssetByCategory(
            WaifuAssetCategory.WELCOMING
        ), asset -> {
            WaifuMotivation waifuMotivation = VisualMotivationFactory.INSTANCE.constructMotivation(
                project,
                asset,
                config
            );

            if ( !project.isInitialized() ) {
                StartupManager.getInstance( project ).registerPostStartupActivity( waifuMotivation::motivate );
            } else {
                waifuMotivation.motivate();
            }
        }, () -> {
            // todo: tell user that this feature is unavailable offline.
        } );

    }

    private boolean isMultipleProjectsOpened() {
        return ProjectManager.getInstance().getOpenProjects().length > 1;
    }

}
