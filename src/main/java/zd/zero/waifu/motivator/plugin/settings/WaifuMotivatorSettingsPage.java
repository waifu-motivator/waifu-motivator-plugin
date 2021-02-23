package zd.zero.waifu.motivator.plugin.settings;

import com.intellij.ide.GeneralSettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zd.zero.waifu.motivator.plugin.WaifuMotivator;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class WaifuMotivatorSettingsPage implements SearchableConfigurable, Configurable.NoScroll {

    private final WaifuMotivatorState state;

    private JPanel rootPanel;

    private JTabbedPane generalSettingsTab;

    private JCheckBox enableWaifuOfTheDay;

    public WaifuMotivatorSettingsPage() {
        this.state = WaifuMotivatorPluginState.getPluginState();
    }

    @NotNull
    @Override
    public String getId() {
        return WaifuMotivator.PLUGIN_ID;
    }

    @Nls
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
        return enableWaifuOfTheDay.isSelected() != this.state.isWaifuOfTheDayEnabled();
    }

    @Override
    public void reset() {
        this.setFieldsFromState();
    }

    @Override
    public void apply() {
        this.state.setWaifuOfTheDayEnabled( enableWaifuOfTheDay.isSelected() );

        // updates the Tip of the Day setting
        GeneralSettings.getInstance().setShowTipsOnStartup( !enableWaifuOfTheDay.isSelected() );

        ApplicationManager.getApplication().getMessageBus()
            .syncPublisher( PluginSettingsListener.Companion.getPLUGIN_SETTINGS_TOPIC() )
            .settingsUpdated( this.state );
    }

    private void setFieldsFromState() {
        this.enableWaifuOfTheDay.setSelected( this.state.isWaifuOfTheDayEnabled() );
    }

    private void createUIComponents() {
    }
}
