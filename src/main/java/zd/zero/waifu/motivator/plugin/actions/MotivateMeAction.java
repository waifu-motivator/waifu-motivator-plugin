package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory;
import zd.zero.waifu.motivator.plugin.motivation.MotivationFactory;
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent;
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory;
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.Objects;

public class MotivateMeAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        WaifuMotivatorState pluginState = WaifuMotivatorPluginState.getPluginState();

        AlertConfiguration config = new AlertConfiguration(
            pluginState.isMotivateMeEnabled() || pluginState.isMotivateMeSoundEnabled(),
            pluginState.isMotivateMeEnabled(),
            pluginState.isMotivateMeSoundEnabled() );


        MotivationFactory.INSTANCE.showUntitledMotivationEventFromCategories(
            new MotivationEvent(
                MotivationEvents.MISC,
                MotivationEventCategory.POSITIVE,
                "Motivate Me",
                Objects.requireNonNull( e.getProject() ),
                () -> config
            ),
            null,
            WaifuAssetCategory.CELEBRATION,
            WaifuAssetCategory.HAPPY,
            WaifuAssetCategory.SMUG
        );
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
