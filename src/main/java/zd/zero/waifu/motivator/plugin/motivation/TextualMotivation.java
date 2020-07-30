package zd.zero.waifu.motivator.plugin.motivation;

import zd.zero.waifu.motivator.plugin.alert.WaifuNotification;
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayer;

public class TextualMotivation extends BaseMotivation {

    public TextualMotivation( WaifuNotification notifier,
                              WaifuSoundPlayer player,
                              AlertConfiguration config ) {
        super( notifier, player, config );
    }
}
