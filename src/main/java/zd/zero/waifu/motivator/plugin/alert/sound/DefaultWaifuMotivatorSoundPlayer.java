package zd.zero.waifu.motivator.plugin.alert.sound;

import com.intellij.openapi.diagnostic.Logger;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

import static zd.zero.waifu.motivator.plugin.WaifuMotivator.SOUND_DIR;

public class DefaultWaifuMotivatorSoundPlayer implements WaifuMotivatorSoundPlayer {

    private static final Logger LOGGER = Logger.getInstance( DefaultWaifuMotivatorSoundPlayer.class );

    private String fileName;

    private Clip clip;

    private DefaultWaifuMotivatorSoundPlayer( String fileName ) {
        this.fileName = SOUND_DIR + fileName;
    }

    public static DefaultWaifuMotivatorSoundPlayer ofFile( String fileName ) {
        return new DefaultWaifuMotivatorSoundPlayer( fileName );
    }

    @Override
    public void play() {
        try ( InputStream soundStream = getClass().getClassLoader().getResourceAsStream( fileName ) ) {
            if ( soundStream == null ) {
                throw new IllegalArgumentException( "Could not create stream for " + fileName );
            }
            clip = SoundClipUtil.openClip( soundStream );
            clip.start();
        } catch ( IOException | LineUnavailableException | UnsupportedAudioFileException e ) {
            LOGGER.error( e.getMessage(), e );
        }
    }

    public void playAndWait() {
        play();
        try {
            Thread.sleep( clip.getMicrosecondLength() / 1000 );
        } catch ( InterruptedException e ) {
            LOGGER.error( e.getMessage(), e );
        }
    }

    @Override
    public void stop() {
        if ( clip == null ) {
            throw new IllegalStateException( "Clip must be started." );
        }
        clip.stop();
    }
}
