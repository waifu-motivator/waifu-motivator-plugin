package zd.zero.waifu.motivator.plugin.motivation;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.alert.TextualWaifuNotification;
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration;
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset;
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

    @NotNull
    @Override
    public WaifuMotivation constructMotivation( @NotNull Project project,
                                                @NotNull MotivationAsset motivation,
                                                @NotNull AlertConfiguration config ) {
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
