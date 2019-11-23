package zd.zero.waifu.motivator.plugin.alert.sound;

import com.intellij.openapi.diagnostic.Logger;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

import static zd.zero.waifu.motivator.plugin.WaifuMotivator.SOUND_DIR;

public class WaifuMotivatorSoundPlayerImpl implements WaifuMotivatorSoundPlayer {

    private static final Logger LOGGER = Logger.getInstance( WaifuMotivatorSoundPlayerImpl.class );

    private String soundFileName;

    public WaifuMotivatorSoundPlayerImpl( String soundFileName ) {
        this.soundFileName = SOUND_DIR + soundFileName;
    }

    @Override
    public void play() {
        try ( InputStream soundStream = getClass().getClassLoader()
                .getResourceAsStream( soundFileName ) ) {

            if ( soundStream == null ) {
                throw new IllegalArgumentException( "Could not create stream for " + soundFileName );
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( soundStream );

            Clip clip = AudioSystem.getClip();
            clip.open( audioInputStream );
            clip.start();

            clip.addLineListener( event -> {
                if ( event.getType() == LineEvent.Type.STOP ) {
                    clip.close();
                }
            } );
        } catch ( IOException | LineUnavailableException | UnsupportedAudioFileException e ) {
            LOGGER.error( e.getMessage(), e );
        }
    }
}
