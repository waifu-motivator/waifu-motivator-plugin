package zd.zero.waifu.motivator.plugin.alert;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayerFactory;

public class TextualMotivationFactory implements WaifuMotivationFactory {

    private static TextualMotivationFactory ourInstance;

    @NotNull
    public static TextualMotivationFactory getInstance() {
        if (ourInstance == null) {
            ourInstance = new TextualMotivationFactory();
        }
        return ourInstance;
    }

    @Override
    public WaifuMotivation constructMotivation( Project project, MotivationAsset motivation, AlertConfiguration config ) {
        return new TextualMotivation(
            new TextualWaifuNotification(
                motivation,
                project
            ),
            WaifuSoundPlayerFactory.createPlayer( motivation.getSoundFileName() ),
            config
        );
    }
}
