package zd.zero.waifu.motivator.plugin.alert.dialog;

import com.intellij.ide.GeneralSettings;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.MessageBundle;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

public class DialogTipPanel implements DialogWrapper.DoNotAskOption {

    private final WaifuMotivatorState pluginState;

    public DialogTipPanel() {
        this.pluginState = WaifuMotivatorPluginState.getInstance().getState();
    }

    @Override
    public boolean isToBeShown() {
        return this.pluginState.isWaifuOfTheDayEnabled();
    }

    @Override
    public void setToBeShown( boolean toBeShown, int exitCode ) {
        pluginState.setWaifuOfTheDayEnabled( toBeShown );
        GeneralSettings.getInstance().setShowTipsOnStartup( !toBeShown );
    }

    @Override
    public boolean canBeHidden() {
        return true;
    }

    @Override
    public boolean shouldSaveOptionsOnCancel() {
        return true;
    }

    @NotNull
    @Override
    public String getDoNotShowMessage() {
        return MessageBundle.message("dialog.tip.panel.dont.show");
    }
}
