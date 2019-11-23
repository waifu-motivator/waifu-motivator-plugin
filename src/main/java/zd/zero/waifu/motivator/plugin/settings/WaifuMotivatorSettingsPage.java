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

    private JCheckBox enableUnitTesterMotivation;

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
        this.enableUnitTesterMotivation.setSelected( this.state.isUnitTesterMotivationEnabled() );
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return enableStartupMotivation.isSelected() != this.state.isStartupMotivationEnabled() ||
                enableUnitTesterMotivation.isSelected() != this.state.isUnitTesterMotivationEnabled();
    }

    @Override
    public void reset() {
        this.enableStartupMotivation.setSelected( this.state.isStartupMotivationEnabled() );
        this.enableUnitTesterMotivation.setSelected( this.state.isUnitTesterMotivationEnabled() );
    }

    @Override
    public void apply() {
        this.state.setStartupMotivationEnabled( enableStartupMotivation.isSelected() );
        this.state.setUnitTesterMotivationEnabled( enableUnitTesterMotivation.isSelected() );
    }

}
