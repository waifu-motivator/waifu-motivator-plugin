package zd.zero.waifu.motivator.plugin.alert;

import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;

public interface WaifuMotivationFactory {

    WaifuMotivation constructMotivation( Project project, MotivationAsset motivation, AlertConfiguration config);

}
