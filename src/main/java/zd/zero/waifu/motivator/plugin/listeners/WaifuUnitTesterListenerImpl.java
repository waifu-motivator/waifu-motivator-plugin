package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlert;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertFactory;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;


public class WaifuUnitTesterListenerImpl implements WaifuUnitTester.Listener {

    private final Project project;

    private final WaifuMotivatorState pluginState;

    public WaifuUnitTesterListenerImpl( Project project ) {
        this.project = project;
        this.pluginState = WaifuMotivatorPluginState.getInstance().getState();
    }

    @Override
    public void onUnitTestPassed() {
        WaifuMotivatorAlert passedAlert = WaifuMotivatorAlertFactory.createAlert( project,
                AlertAssetProvider.getRandomAssetByCategory( WaifuMotivatorAlertAssetCategory.POSITIVE ), getUnitTesterConfiguration() );
        passedAlert.alert();
    }

    @Override
    public void onUnitTestFailed() {
        WaifuMotivatorAlert failedAlert = WaifuMotivatorAlertFactory.createAlert( project,
                AlertAssetProvider.getRandomAssetByCategory( WaifuMotivatorAlertAssetCategory.NEGATIVE ), getUnitTesterConfiguration() );
        failedAlert.alert();
    }

    private AlertConfiguration getUnitTesterConfiguration() {
        return AlertConfiguration.builder()
                .isAlertEnabled( pluginState.isUnitTesterMotivationEnabled() || pluginState.isUnitTesterMotivationSoundEnabled() )
                .isDisplayNotificationEnabled( pluginState.isUnitTesterMotivationEnabled() )
                .isSoundAlertEnabled( pluginState.isUnitTesterMotivationSoundEnabled() )
                .build();
    }
}
