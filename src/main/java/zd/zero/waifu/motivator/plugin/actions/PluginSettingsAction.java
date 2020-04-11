package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorSettingsPage;

public class PluginSettingsAction extends AnAction {
    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        if ( e.getProject() == null ) return;

        ShowSettingsUtil.getInstance().showSettingsDialog( e.getProject(), WaifuMotivatorSettingsPage.class );
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
