package zd.zero.waifu.motivator.plugin.settings;

public class WaifuMotivatorState {

    private boolean isStartupMotivationEnabled = true;

    public boolean isStartupMotivationEnabled() {
        return this.isStartupMotivationEnabled;
    }

    public void setStartupMotivationEnabled( boolean startupMotivationEnabled ) {
        this.isStartupMotivationEnabled = startupMotivationEnabled;
    }

}
