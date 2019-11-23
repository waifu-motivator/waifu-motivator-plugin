package zd.zero.waifu.motivator.plugin.alert.notification;

import com.intellij.openapi.project.Project;

public class WaifuMotivatorNotifierImpl implements WaifuMotivatorNotifier {

    private final Project project;

    private final String title;

    private final String content;

    public WaifuMotivatorNotifierImpl( Project project, String title, String content ) {
        this.project = project;
        this.title = title;
        this.content = content;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }
}
