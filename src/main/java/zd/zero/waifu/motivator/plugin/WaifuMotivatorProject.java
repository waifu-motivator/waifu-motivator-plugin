package zd.zero.waifu.motivator.plugin;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class WaifuMotivatorProject implements ProjectManagerListener, Disposable {

    private Project project;

    @Override
    public void projectOpened( @NotNull Project projectOpened ) {
        if ( this.project != null ) return;
        project = projectOpened;
    }

    @Override
    public void projectClosing( @NotNull Project project ) {
        if ( project == this.project ) dispose();
    }

    @Override
    public void dispose() {
    }

}
