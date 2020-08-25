package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider;
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory;
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory;
import zd.zero.waifu.motivator.plugin.motivation.WaifuMotivation;
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.Objects;

public class MotivateMeAction extends AnAction {

    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        WaifuMotivatorState pluginState = WaifuMotivatorPluginState.getPluginState();

        AlertConfiguration config = new AlertConfiguration(
            pluginState.isMotivateMeEnabled() || pluginState.isMotivateMeSoundEnabled(),
            pluginState.isMotivateMeEnabled(),
            pluginState.isMotivateMeSoundEnabled() );

        WaifuMotivation waifuMotivation = VisualMotivationFactory.INSTANCE.constructMotivation(
            Objects.requireNonNull( e.getProject() ),
            VisualMotivationAssetProvider.INSTANCE.createAssetByCategory( WaifuAssetCategory.CELEBRATION),
            config
        );
        waifuMotivation.motivate();
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
