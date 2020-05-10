package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertFactory;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

public class MotivateMeAction extends AnAction {

    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        WaifuMotivatorState pluginState = WaifuMotivatorPluginState.getPluginState();

        AlertConfiguration config = AlertConfiguration.builder()
                .isAlertEnabled( pluginState.isMotivateMeEnabled() || pluginState.isMotivateMeSoundEnabled() )
                .isDisplayNotificationEnabled( pluginState.isMotivateMeEnabled() )
                .isSoundAlertEnabled( pluginState.isMotivateMeSoundEnabled() )
                .build();

        WaifuMotivatorAlert motivatorAlert = WaifuMotivatorAlertFactory.createAlert( e.getProject(),
                AlertAssetProvider.getRandomAssetByCategory( WaifuMotivatorAlertAssetCategory.NEUTRAL ), config );
        motivatorAlert.alert();
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
