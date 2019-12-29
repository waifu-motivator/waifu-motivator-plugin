package zd.zero.waifu.motivator.plugin.alert;

import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.notification.DefaultWaifuMotivatorNotifier;
import zd.zero.waifu.motivator.plugin.alert.sound.DefaultWaifuMotivatorSoundPlayer;

public interface WaifuMotivatorAlertFactory {

    static WaifuMotivatorAlert createAlert( Project project, WaifuMotivatorAlertAsset asset, AlertConfiguration config ) {
        return new WaifuMotivatorAlertImpl(
                new DefaultWaifuMotivatorNotifier( project, asset.getTitle(), asset.getMessage() ),
                new DefaultWaifuMotivatorSoundPlayer( asset.getSound() ),
                config
        );
    }

}
