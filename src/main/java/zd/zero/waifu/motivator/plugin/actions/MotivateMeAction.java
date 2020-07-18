package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.TextualMotivationFactory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivation;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

public class MotivateMeAction extends AnAction {

    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        WaifuMotivatorState pluginState = WaifuMotivatorPluginState.getPluginState();

        AlertConfiguration config = new AlertConfiguration(
            pluginState.isMotivateMeEnabled() || pluginState.isMotivateMeSoundEnabled(),
            pluginState.isMotivateMeEnabled(),
            pluginState.isMotivateMeSoundEnabled() );

        WaifuMotivation waifuMotivation = TextualMotivationFactory.getInstance().constructMotivation( e.getProject(),
                AlertAssetProvider.getRandomAssetByCategory( WaifuMotivatorAlertAssetCategory.NEUTRAL ), config );
        waifuMotivation.motivate();
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
