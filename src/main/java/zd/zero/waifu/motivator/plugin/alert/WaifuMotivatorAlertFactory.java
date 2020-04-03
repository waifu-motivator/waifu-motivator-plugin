package zd.zero.waifu.motivator.plugin.alert;

import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.alert.notification.DefaultWaifuMotivatorNotifier;
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory;

public interface WaifuMotivatorAlertFactory {

    static WaifuMotivatorAlert createAlert( Project project, WaifuMotivatorAlertAsset asset, AlertConfiguration config ) {
        return new WaifuMotivatorAlertImpl(
                DefaultWaifuMotivatorNotifier.builder().project( project )
                        .title( asset.getTitle() )
                        .content( StringUtils.defaultIfEmpty( asset.getMessage(), "" ) )
                        .build(),
                WaifuSoundPlayerFactory.createPlayer( asset.getSoundFileName() ),
                config
        );
    }

}
