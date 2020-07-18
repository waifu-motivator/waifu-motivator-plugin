package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.TextualMotivationFactory;
import zd.zero.waifu.motivator.plugin.alert.VisualMotivationAssetProvider;
import zd.zero.waifu.motivator.plugin.alert.VisualMotivationFactory;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivation;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
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
        WaifuMotivation successMotivation = VisualMotivationFactory.INSTANCE.constructMotivation( project,
            VisualMotivationAssetProvider.INSTANCE.pickRandomByCategory( WaifuMotivatorAlertAssetCategory.POSITIVE ),
            getUnitTesterConfiguration() );
        successMotivation.motivate();
    }

    @Override
    public void onUnitTestFailed() {
        WaifuMotivation keepGoingMotivation = TextualMotivationFactory.getInstance().constructMotivation( project,
                AlertAssetProvider.getRandomAssetByCategory( WaifuMotivatorAlertAssetCategory.NEGATIVE ), 
            getUnitTesterConfiguration() );
        keepGoingMotivation.motivate();
    }

    private AlertConfiguration getUnitTesterConfiguration() {
        return AlertConfiguration.builder()
                .isAlertEnabled( pluginState.isUnitTesterMotivationEnabled() || pluginState.isUnitTesterMotivationSoundEnabled() )
                .isDisplayNotificationEnabled( pluginState.isUnitTesterMotivationEnabled() )
                .isSoundAlertEnabled( pluginState.isUnitTesterMotivationSoundEnabled() )
                .build();
    }
}
