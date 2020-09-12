package zd.zero.waifu.motivator.plugin.settings;

import com.intellij.ide.GeneralSettings;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.cellvalidators.CellComponentProvider;
import com.intellij.openapi.ui.cellvalidators.CellTooltipManager;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zd.zero.waifu.motivator.plugin.WaifuMotivator;
import zd.zero.waifu.motivator.plugin.service.ApplicationService;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WaifuMotivatorSettingsPage implements SearchableConfigurable, Configurable.NoScroll {

    private final WaifuMotivatorState state;

    private JPanel rootPanel;

    private JCheckBox enableWaifuOfTheDay;

    private JCheckBox disabledInDistractionFreeMode;

    private JCheckBox enableStartupMotivation;

    private JCheckBox enableUnitTesterMotivation;

    private JCheckBox enableMotivateMe;

    private JCheckBox enableSayonara;

    private JCheckBox enableStartupMotivationSound;

    private JCheckBox enableUnitTesterMotivationSound;

    private JCheckBox enableMotivateMeSound;

    private JTabbedPane idleEventTab;

    private JCheckBox enableIdleNotificationCheckBox;

    private JCheckBox enableIdleSoundCheckBox;

    private JSpinner idleTimeoutSpinner;

    private JCheckBox enableTaskEventNotificationsCheckBox;

    private JCheckBox enableTaskEventSoundsCheckBox;

    private JSpinner eventsBeforeFrustrationSpinner;

    private JSlider frustrationProbabilitySlider;

    private JCheckBox allowFrustrationCheckBox;

    private JCheckBox enableExitCodeNotifications;

    private JCheckBox enableExitCodeSound;

    private JBTable exitCodes;

    private JPanel exitCodePanel;

    private JLabel allowedExitCodeLabel;

    private ListTableModel<String> exitCodeModel;

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
        idleTimeoutSpinner.setModel( new SpinnerNumberModel( 1, 1, null, 1 ) );
        eventsBeforeFrustrationSpinner.setModel( new SpinnerNumberModel( 5, 0, null, 1 ) );
        allowFrustrationCheckBox.addActionListener( e -> {
            frustrationProbabilitySlider.setEnabled(allowFrustrationCheckBox.isSelected());
            eventsBeforeFrustrationSpinner.setEnabled(allowFrustrationCheckBox.isSelected());
        } );
        this.setFieldsFromState();
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return enableWaifuOfTheDay.isSelected() != this.state.isWaifuOfTheDayEnabled() ||
                disabledInDistractionFreeMode.isSelected() != this.state.isDisabledInDistractionFreeMode() ||
                enableStartupMotivation.isSelected() != this.state.isStartupMotivationEnabled() ||
                enableStartupMotivationSound.isSelected() != this.state.isStartupMotivationSoundEnabled() ||
                enableUnitTesterMotivation.isSelected() != this.state.isUnitTesterMotivationEnabled() ||
                enableUnitTesterMotivationSound.isSelected() != this.state.isUnitTesterMotivationSoundEnabled() ||
                enableMotivateMe.isSelected() != this.state.isMotivateMeEnabled() ||
                enableMotivateMeSound.isSelected() != this.state.isMotivateMeSoundEnabled() ||
                enableIdleNotificationCheckBox.isSelected() != this.state.isIdleMotivationEnabled() ||
                enableIdleSoundCheckBox.isSelected() != this.state.isIdleSoundEnabled() ||
                getIdleTimeout() != this.state.getIdleTimeoutInMinutes() ||
                enableTaskEventNotificationsCheckBox.isSelected() != this.state.isTaskMotivationEnabled() ||
                allowFrustrationCheckBox.isSelected() != this.state.isAllowFrustration() ||
                enableTaskEventSoundsCheckBox.isSelected() != this.state.isTaskSoundEnabled() ||
                frustrationProbabilitySlider.getValue() != this.state.getProbabilityOfFrustration() ||
                ((Integer) eventsBeforeFrustrationSpinner.getValue()) != this.state.getEventsBeforeFrustration() ||
                enableExitCodeNotifications.isSelected() != this.state.isExitCodeNotificationEnabled() ||
                enableExitCodeSound.isSelected() != this.state.isExitCodeSoundEnabled() ||
                enableSayonara.isSelected() != this.state.isSayonaraEnabled() ||
                codesChanged;
    }

    private long getIdleTimeout() {
        Object timeoutValue = idleTimeoutSpinner.getValue();
        if(timeoutValue instanceof Long) {
            return (Long)timeoutValue;
        } else if (timeoutValue instanceof Integer) {
            return Long.valueOf( (Integer) timeoutValue );
        }
        return WaifuMotivatorState.DEFAULT_IDLE_TIMEOUT_IN_MINUTES;
    }

    @Override
    public void reset() {
        this.setFieldsFromState();
    }

    @Override
    public void apply() {
        this.state.setWaifuOfTheDayEnabled( enableWaifuOfTheDay.isSelected() );
        this.state.setDisabledInDistractionFreeMode( disabledInDistractionFreeMode.isSelected() );
        this.state.setStartupMotivationEnabled( enableStartupMotivation.isSelected() );
        this.state.setStartupMotivationSoundEnabled( enableStartupMotivationSound.isSelected() );
        this.state.setUnitTesterMotivationEnabled( enableUnitTesterMotivation.isSelected() );
        this.state.setUnitTesterMotivationSoundEnabled( enableUnitTesterMotivationSound.isSelected() );
        this.state.setMotivateMeEnabled( enableMotivateMe.isSelected() );
        this.state.setMotivateMeSoundEnabled( enableMotivateMeSound.isSelected() );
        this.state.setSayonaraEnabled( enableSayonara.isSelected() );
        this.state.setIdleMotivationEnabled( enableIdleNotificationCheckBox.isSelected() );
        this.state.setIdleSoundEnabled( enableIdleSoundCheckBox.isSelected() );
        this.state.setIdleTimeoutInMinutes( getIdleTimeout() );
        this.state.setTaskMotivationEnabled( enableTaskEventNotificationsCheckBox.isSelected() );
        this.state.setTaskSoundEnabled( enableTaskEventSoundsCheckBox.isSelected() );
        this.state.setAllowFrustration( allowFrustrationCheckBox.isSelected() );
        this.state.setEventsBeforeFrustration( ( Integer ) eventsBeforeFrustrationSpinner.getValue() );
        this.state.setProbabilityOfFrustration( frustrationProbabilitySlider.getValue() );
        this.state.setExitCodeNotificationEnabled( enableExitCodeNotifications.isSelected() );
        this.state.setExitCodeSoundEnabled( enableExitCodeSound.isSelected() );
        this.state.setAllowedExitCodes(
            IntStream.range( 0, exitCodeModel.getRowCount() )
            .mapToObj( exitCodeModel::getRowValue )
            .mapToInt( Integer::parseInt )
            .distinct()
            .sorted()
            .mapToObj( String::valueOf )
            .collect( Collectors.joining(WaifuMotivatorState.DEFAULT_DELIMITER))
        );

        // updates the Tip of the Day setting
        GeneralSettings.getInstance().setShowTipsOnStartup( !enableWaifuOfTheDay.isSelected() );

        ApplicationManager.getApplication().getMessageBus()
            .syncPublisher( PluginSettingsListener.Companion.getPLUGIN_SETTINGS_TOPIC() )
            .settingsUpdated( this.state );
    }

    private void setFieldsFromState() {
        this.enableWaifuOfTheDay.setSelected( this.state.isWaifuOfTheDayEnabled() );
        this.disabledInDistractionFreeMode.setSelected( this.state.isDisabledInDistractionFreeMode() );
        this.enableStartupMotivation.setSelected( this.state.isStartupMotivationEnabled() );
        this.enableUnitTesterMotivation.setSelected( this.state.isUnitTesterMotivationEnabled() );
        this.enableMotivateMe.setSelected( this.state.isMotivateMeEnabled() );
        this.enableSayonara.setSelected( this.state.isSayonaraEnabled() );
        this.enableStartupMotivationSound.setSelected( this.state.isStartupMotivationSoundEnabled() );
        this.enableUnitTesterMotivationSound.setSelected( this.state.isUnitTesterMotivationSoundEnabled() );
        this.enableMotivateMeSound.setSelected( this.state.isMotivateMeSoundEnabled() );
        this.idleTimeoutSpinner.setValue( this.state.getIdleTimeoutInMinutes() );
        this.enableIdleNotificationCheckBox.setSelected( this.state.isIdleMotivationEnabled() );
        this.enableIdleSoundCheckBox.setSelected( this.state.isIdleSoundEnabled() );
        this.enableTaskEventNotificationsCheckBox.setSelected( this.state.isTaskMotivationEnabled() );
        this.enableTaskEventSoundsCheckBox.setSelected( this.state.isTaskSoundEnabled() );
        this.allowFrustrationCheckBox.setSelected( this.state.isAllowFrustration() );
        this.eventsBeforeFrustrationSpinner.setValue( this.state.getEventsBeforeFrustration() );
        this.frustrationProbabilitySlider.setValue( this.state.getProbabilityOfFrustration() );
        this.enableExitCodeNotifications.setSelected( this.state.isExitCodeNotificationEnabled() );
        this.enableExitCodeSound.setSelected( this.state.isExitCodeSoundEnabled() );

        int rowCount = exitCodeModel.getRowCount();
        if(rowCount > 0) {
            IntStream.range( 0, rowCount).forEach( idx -> exitCodeModel.removeRow( 0 ) );
        }
        Arrays.stream( this.state.getAllowedExitCodes().split( WaifuMotivatorState.DEFAULT_DELIMITER ) )
            .map( Integer::parseInt )
            .map( String::valueOf )
            .forEach( exitCodeModel::addRow );
        codesChanged = false;
    }

    private boolean codesChanged = false;

    private void createUIComponents() {
        exitCodeModel = new ListTableModel<String>(  ) {
            @Override
            public void addRow() {
                addRow("0");
            }

        };
        exitCodeModel.addTableModelListener( e -> codesChanged = true );

        exitCodes = new JBTable(exitCodeModel);
        exitCodeModel.setColumnInfos(new ColumnInfo[]{new ColumnInfo<String, String>("Exit Code") {

            @Override
            public String valueOf( String integer ) {
                return integer.toString();
            }

            @Override
            public boolean isCellEditable( String info) {
                return true;
            }

        }});
        exitCodes.getColumnModel().setColumnMargin(0);
        exitCodes.setShowColumns(false);
        exitCodes.setShowGrid(false);

        exitCodes.getEmptyText().setText( IdeBundle.message("update.no.update.hosts"));

        exitCodes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        new CellTooltipManager(ApplicationService.INSTANCE).
            withCellComponentProvider( CellComponentProvider.forTable(exitCodes)).
            installOn(exitCodes);


        exitCodePanel = ToolbarDecorator.createDecorator( exitCodes )
            .disableUpDownActions().createPanel();

    }
}
