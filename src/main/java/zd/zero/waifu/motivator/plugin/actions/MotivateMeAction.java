package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.ProjectConstants;
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider;
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory;
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory;
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import java.util.Objects;

import static zd.zero.waifu.motivator.plugin.tools.ToolBox.doOrElse;

public class MotivateMeAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        WaifuMotivatorState pluginState = WaifuMotivatorPluginState.getPluginState();

        AlertConfiguration config = new AlertConfiguration(
            pluginState.isMotivateMeEnabled() || pluginState.isMotivateMeSoundEnabled(),
            pluginState.isMotivateMeEnabled(),
            pluginState.isMotivateMeSoundEnabled() );

        doOrElse(
            VisualMotivationAssetProvider.INSTANCE.pickAssetFromCategories(
                WaifuAssetCategory.CELEBRATION,
                WaifuAssetCategory.HAPPY,
                WaifuAssetCategory.SMUG
            ),
            motivationAsset ->
                VisualMotivationFactory.INSTANCE.constructMotivation(
                    Objects.requireNonNull( e.getProject() ),
                    motivationAsset,
                    config
                ).motivate(),
            () ->
                UpdateNotification.INSTANCE.sendMessage(
                    "'Motivate Me' Unavailable Offline",
                    ProjectConstants.getWAIFU_UNAVAILABLE_MESSAGE(),
                    e.getProject()
                ) );
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
