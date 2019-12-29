package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertFactory;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;

public class MotivateMeAction extends AnAction {

    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        AlertConfiguration config = AlertConfiguration.builder()
                .isAlertEnabled( true )
                .isDisplayNotificationEnabled( true )
                .isSoundAlertEnabled( true )
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
