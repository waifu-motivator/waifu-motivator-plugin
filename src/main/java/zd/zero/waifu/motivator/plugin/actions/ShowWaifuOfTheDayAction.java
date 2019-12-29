package zd.zero.waifu.motivator.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.dialog.WaifuOfTheDayDialog;

public class ShowWaifuOfTheDayAction extends AnAction {
    @Override
    public void actionPerformed( @NotNull AnActionEvent e ) {
        WaifuOfTheDayDialog.show( e.getProject() );
    }

    @Override
    public void update( @NotNull AnActionEvent e ) {
        e.getPresentation().setEnabledAndVisible( e.getProject() != null );
    }
}
