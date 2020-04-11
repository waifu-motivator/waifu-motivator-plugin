package zd.zero.waifu.motivator.plugin.settings;

import com.intellij.ide.GeneralSettings;
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

    private JCheckBox enableWaifuOfTheDay;

    private JCheckBox enableSayonara;

    private JCheckBox enableStartupMotivationSound;

    private JCheckBox enableUnitTesterMotivationSound;

    private JCheckBox enableMotivateMeSound;

    public WaifuMotivatorSettingsPage() {
        this.state = WaifuMotivatorPluginState.getPluginState();
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
        this.setFieldsFromState();
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return enableWaifuOfTheDay.isSelected() != this.state.isWaifuOfTheDayEnabled() ||
                enableStartupMotivation.isSelected() != this.state.isStartupMotivationEnabled() ||
                enableUnitTesterMotivation.isSelected() != this.state.isUnitTesterMotivationEnabled() ||
                enableSayonara.isSelected() != this.state.isSayonaraEnabled() ||
                enableStartupMotivationSound.isSelected() != this.state.isStartupMotivationSoundEnabled() ||
                enableUnitTesterMotivationSound.isSelected() != this.state.isUnitTesterMotivationSoundEnabled() ||
                enableMotivateMeSound.isSelected() != this.state.isMotivateMeSoundEnabled();
    }

    @Override
    public void reset() {
        this.setFieldsFromState();
    }

    @Override
    public void apply() {
        this.state.setStartupMotivationEnabled( enableStartupMotivation.isSelected() );
        this.state.setUnitTesterMotivationEnabled( enableUnitTesterMotivation.isSelected() );
        this.state.setSayonaraEnabled( enableSayonara.isSelected() );

        this.state.setWaifuOfTheDayEnabled( enableWaifuOfTheDay.isSelected() );
        this.state.setStartupMotivationSoundEnabled( enableStartupMotivationSound.isSelected() );
        this.state.setUnitTesterMotivationSoundEnabled( enableUnitTesterMotivationSound.isSelected() );
        this.state.setMotivateMeSoundEnabled( enableMotivateMeSound.isSelected() );
        GeneralSettings.getInstance().setShowTipsOnStartup( !enableWaifuOfTheDay.isSelected() );
    }

    private void setFieldsFromState() {
        this.enableWaifuOfTheDay.setSelected( this.state.isWaifuOfTheDayEnabled() );
        this.enableStartupMotivation.setSelected( this.state.isStartupMotivationEnabled() );
        this.enableUnitTesterMotivation.setSelected( this.state.isUnitTesterMotivationEnabled() );
        this.enableSayonara.setSelected( this.state.isSayonaraEnabled() );
        this.enableStartupMotivationSound.setSelected( this.state.isStartupMotivationSoundEnabled() );
        this.enableUnitTesterMotivationSound.setSelected( this.state.isUnitTesterMotivationSoundEnabled() );
        this.enableMotivateMeSound.setSelected( this.state.isMotivateMeSoundEnabled() );
    }

}
