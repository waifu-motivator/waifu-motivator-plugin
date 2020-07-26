package zd.zero.waifu.motivator.plugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "WaifuMotivatorPluginSettings",
        storages = @Storage( "waifu-motivator-plugin.xml" )
)
public class WaifuMotivatorPluginState implements PersistentStateComponent<WaifuMotivatorState> {

    private final WaifuMotivatorState state = new WaifuMotivatorState();

    public static WaifuMotivatorPluginState getInstance() {
        return ApplicationManager.getApplication().getService( WaifuMotivatorPluginState.class );
    }

    public static WaifuMotivatorState getPluginState() {
        return getInstance().getState();
    }

    @Nullable
    @Override
    public WaifuMotivatorState getState() {
        return this.state;
    }

    @Override
    public void loadState( @NotNull WaifuMotivatorState state ) {
        XmlSerializerUtil.copyBean( state, this.state );
    }

}
