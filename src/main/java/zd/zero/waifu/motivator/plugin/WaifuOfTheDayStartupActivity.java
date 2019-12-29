package zd.zero.waifu.motivator.plugin;

import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.dialog.WaifuOfTheDayDialog;

public class WaifuOfTheDayStartupActivity implements StartupActivity.DumbAware {
    @Override
    public void runActivity( @NotNull Project project ) {
        Alarm alarm = new Alarm( project );
        alarm.addRequest( () -> WaifuOfTheDayDialog.canBeShownToday( project ),
                0, ModalityState.any() );
    }
}
