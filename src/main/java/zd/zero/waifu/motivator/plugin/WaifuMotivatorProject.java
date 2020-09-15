package zd.zero.waifu.motivator.plugin;

import com.intellij.ide.GeneralSettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationInfo;
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
import zd.zero.waifu.motivator.plugin.listeners.ExitCodeListener;
import zd.zero.waifu.motivator.plugin.listeners.IdleEventListener;
import zd.zero.waifu.motivator.plugin.listeners.PluginInstallListener;
import zd.zero.waifu.motivator.plugin.listeners.WaifuUnitTester;
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory;
import zd.zero.waifu.motivator.plugin.motivation.WaifuMotivation;
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification;
import zd.zero.waifu.motivator.plugin.onboarding.UserOnboarding;
import zd.zero.waifu.motivator.plugin.platform.LifeCycleManager;
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState.getPluginState;
import static zd.zero.waifu.motivator.plugin.tools.ToolBox.doOrElse;

public class WaifuMotivatorProject implements ProjectManagerListener, Disposable {

    private static final String IS_INITIAL_PLATFORM_TIP_UPDATED = "WAIFU_UPDATE_TIP";

    private Project project;

    private WaifuUnitTester unitTestListener;

    private IdleEventListener idleEventListener;

    private ExitCodeListener exitCodeListener;

    @Override
    public void projectOpened( @NotNull Project projectOpened ) {
        LifeCycleManager.INSTANCE.init();
        if ( this.project != null ) return;

        checkIfInGoodState( projectOpened, () -> {
            this.project = projectOpened;
            this.unitTestListener = WaifuUnitTester.newInstance( projectOpened );
            this.idleEventListener = new IdleEventListener( projectOpened );
            this.exitCodeListener = new ExitCodeListener( projectOpened );

            updatePlatformStartupConfig();
            initializeListeners();
            initializeStartupMotivator();
            UserOnboarding.INSTANCE.attemptToPerformNewUpdateActions();
        } );
    }

    private void checkIfInGoodState( Project projectOpened, Runnable onGoodState ) {
        StartupManager.getInstance( projectOpened ).registerPostStartupActivity( () -> {
            boolean isInGoodState = Stream.of(
                TextAssetManager.INSTANCE,
                VisualAssetManager.INSTANCE,
                AudibleAssetManager.INSTANCE
            ).map( HasStatus::getStatus )
                .allMatch( Predicate.isEqual( Status.OK ) );
            if ( !isInGoodState ) {
                UpdateNotification.INSTANCE.sendMessage(
                    "Unable to contact Waifus!",
                    "I need internet first before I can bring you waifus. " +
                        "Please re-establish connection and restart " +
                        ApplicationInfo.getInstance().getFullApplicationName()
                        + ".",
                    projectOpened
                );
            } else {
                onGoodState.run();
            }
        } );
    }

    @Override
    public void projectClosing( @NotNull Project project ) {
        if(project == this.project) {
            dispose();
        }

        if ( !getPluginState().isSayonaraEnabled() ||
            areMultipleProjectsOpened() ||
            PluginInstallListener.Companion.isRunningUpdate() ) return;

        AudibleAssetDefinitionService.INSTANCE.getRandomAssetByCategory(
            WaifuAssetCategory.DEPARTURE
        ).ifPresent( asset -> WaifuSoundPlayerFactory.createPlayer( asset.getSoundFilePath() ).playAndWait() );
    }

    @Override
    public void dispose() {
        ofNullable( this.unitTestListener ).ifPresent( WaifuUnitTester::stop );
        ofNullable( this.idleEventListener ).ifPresent( IdleEventListener::dispose );
        ofNullable( this.exitCodeListener ).ifPresent( ExitCodeListener::dispose );
    }

    private void initializeListeners() {
        this.unitTestListener.init();
    }

    private void updatePlatformStartupConfig() {
        boolean isInitialPlatformTipUpdated = Boolean.parseBoolean( PropertiesComponent.getInstance()
            .getValue( IS_INITIAL_PLATFORM_TIP_UPDATED, "" ) );
        if ( !isInitialPlatformTipUpdated ) {
            PropertiesComponent.getInstance().setValue( IS_INITIAL_PLATFORM_TIP_UPDATED, true );
            GeneralSettings.getInstance().setShowTipsOnStartup( !getPluginState().isWaifuOfTheDayEnabled() );
        }
    }

    private void initializeStartupMotivator() {
        if ( areMultipleProjectsOpened()
            || UserOnboarding.INSTANCE.isNewVersion() ) return;

        WaifuMotivatorState pluginState = getPluginState();
        AlertConfiguration config = new AlertConfiguration(
            pluginState.isStartupMotivationEnabled() || pluginState.isStartupMotivationSoundEnabled(),
            pluginState.isStartupMotivationEnabled(),
            pluginState.isStartupMotivationSoundEnabled()
        );

        doOrElse(
            VisualMotivationAssetProvider.INSTANCE.createAssetByCategory(
                WaifuAssetCategory.WELCOMING
            ),
            asset -> {
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
            },
            () -> UpdateNotification.INSTANCE.sendMessage(
                "'Welcome Wafiu' Unavailable Offline",
                ProjectConstants.getWAIFU_UNAVAILABLE_MESSAGE(),
                project
            ) );
    }

    private boolean areMultipleProjectsOpened() {
        return ProjectManager.getInstance().getOpenProjects().length > 1;
    }

}
