package zd.zero.waifu.motivator.plugin.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zd.zero.waifu.motivator.plugin.WaifuMotivator;

import javax.swing.*;

public class WaifuMotivatorSettingsPage implements SearchableConfigurable, Configurable.NoScroll {

    private WaifuMotivatorState state;

    private JPanel rootPanel;

    private JCheckBox enableStartupMotivation;

    public WaifuMotivatorSettingsPage() {
        this.state = WaifuMotivatorPluginSettings.getInstance().getState();
    }

    @NotNull
    @Override
    public String getId() {
        return WaifuMotivator.PLUGIN_ID;
    }

    @Nls( capitalization = Nls.Capitalization.Title )
    @Override
    public String getDisplayName() {
        return WaifuMotivator.PLUGIN_NAME;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        this.enableStartupMotivation.setSelected( this.state.isStartupMotivationEnabled() );
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return enableStartupMotivation.isSelected() != this.state.isStartupMotivationEnabled();
    }

    @Override
    public void reset() {
        this.enableStartupMotivation.setSelected( this.state.isStartupMotivationEnabled() );
    }

    @Override
    public void apply() {
        this.state.setStartupMotivationEnabled( enableStartupMotivation.isSelected() );
    }

}
