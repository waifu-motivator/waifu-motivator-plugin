package zd.zero.waifu.motivator.plugin.alert.notification;

import com.intellij.openapi.project.Project;
import lombok.Builder;

@Builder
public class DefaultWaifuMotivatorNotifier implements WaifuMotivatorNotifier {

    private final Project project;

    private final String title;

    private final String content;

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
