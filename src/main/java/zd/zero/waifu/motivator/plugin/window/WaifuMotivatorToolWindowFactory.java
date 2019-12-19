package zd.zero.waifu.motivator.plugin.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class WaifuMotivatorToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent( @NotNull Project project, @NotNull ToolWindow toolWindow ) {
        WaifuMotivatorToolWindow waifuMotivatorToolWindow = new WaifuMotivatorToolWindow();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent( waifuMotivatorToolWindow.getContent(),
                "", false );
        toolWindow.getContentManager().addContent( content );
    }
}
