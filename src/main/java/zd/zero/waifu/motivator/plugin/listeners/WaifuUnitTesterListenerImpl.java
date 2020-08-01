package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.alert.AlertAssetProvider;
import zd.zero.waifu.motivator.plugin.motivation.TextualMotivationFactory;
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider;
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory;
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory;
import zd.zero.waifu.motivator.plugin.motivation.WaifuMotivation;
import zd.zero.waifu.motivator.plugin.alert.WaifuMotivatorAlertAssetCategory;
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration;
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
            VisualMotivationAssetProvider.INSTANCE.pickAssetFromCategories(
                WaifuAssetCategory.CELEBRATION
//                ,
//                WaifuAssetCategory.SMUG,
//                WaifuAssetCategory.MOTIVATION,
//                WaifuAssetCategory.ENCOURAGEMENT
            ),
            getUnitTesterConfiguration() );
        successMotivation.motivate();
    }

    @Override
    public void onUnitTestFailed() {
        WaifuMotivation keepGoingMotivation = VisualMotivationFactory.INSTANCE.constructMotivation( project,
            VisualMotivationAssetProvider.INSTANCE.pickAssetFromCategories(
                WaifuAssetCategory.DISAPPOINTMENT
//                ,
//                WaifuAssetCategory.ENCOURAGEMENT,
//                WaifuAssetCategory.MOTIVATION
            ),
            getUnitTesterConfiguration() );
        keepGoingMotivation.motivate();
    }

    private AlertConfiguration getUnitTesterConfiguration() {
        return new AlertConfiguration(
            pluginState.isUnitTesterMotivationEnabled() || pluginState.isUnitTesterMotivationSoundEnabled(),
            pluginState.isUnitTesterMotivationEnabled(),
            pluginState.isUnitTesterMotivationSoundEnabled()
        );
    }
}
