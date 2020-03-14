package zd.zero.waifu.motivator.plugin.player;

import com.intellij.openapi.diagnostic.Logger;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

import static zd.zero.waifu.motivator.plugin.WaifuMotivator.SOUND_DIR;

public final class DefaultWaifuSoundPlayer implements WaifuSoundPlayer {

    private static final Logger LOGGER = Logger.getInstance( DefaultWaifuSoundPlayer.class );

    private String fileName;

    private Clip clip;

    private DefaultWaifuSoundPlayer( String fileName ) {
        this.fileName = SOUND_DIR + fileName;
    }

    public static DefaultWaifuSoundPlayer ofFile( String fileName ) {
        return new DefaultWaifuSoundPlayer( fileName );
    }

    @Override
    public void play() {
        try ( InputStream soundStream = getClass().getClassLoader().getResourceAsStream( fileName ) ) {
            if ( soundStream == null ) {
                throw new IllegalArgumentException( "Could not create a stream for " + fileName );
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
