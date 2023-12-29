package zd.zero.waifu.motivator.plugin;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.startup.StartupManager;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.onboarding.UserOnboarding;

public class WaifuMotivatorProject implements ProjectManagerListener, Disposable {

    private Project project;

    @Override
    public void projectOpened(@NotNull Project projectOpened) {
        if (this.project != null) return;
        project = projectOpened;
        UserOnboarding.INSTANCE.attemptToPerformNewUpdateActions();
    }

    @Override
    public void projectClosing( @NotNull Project project ) {
        if ( project == this.project ) dispose();
    }

    @Override
    public void dispose() {
    }

}
