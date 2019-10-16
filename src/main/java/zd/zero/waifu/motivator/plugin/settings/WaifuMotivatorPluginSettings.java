package zd.zero.waifu.motivator.plugin.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "WaifuMotivatorPluginSettings",
        storages = @Storage( "waifu-motivator-plugin.xml" )
)
public class WaifuMotivatorPluginSettings implements PersistentStateComponent<WaifuMotivatorState> {

    private WaifuMotivatorState state = new WaifuMotivatorState();

    public static WaifuMotivatorPluginSettings getInstance() {
        return ServiceManager.getService( WaifuMotivatorPluginSettings.class );
    }

    @Nullable
    @Override
    public WaifuMotivatorState getState() {
        return this.state;
    }

    @Override
    public void loadState( @NotNull WaifuMotivatorState state ) {
        this.state = state;
    }

}
