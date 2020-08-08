package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.task.ProjectTaskListener;
import com.intellij.task.ProjectTaskManager;
import org.jetbrains.annotations.NotNull;

public class WaifuCompilationListener implements ProjectTaskListener {

    @Override
    public void finished( @NotNull ProjectTaskManager.Result result ) {
        if ( result.hasErrors() ) System.out.println( "Build Failed!" );
        else System.out.println( "Yay!" );
    }

}
