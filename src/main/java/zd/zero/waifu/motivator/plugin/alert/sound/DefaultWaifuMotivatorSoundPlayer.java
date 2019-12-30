package zd.zero.waifu.motivator.plugin.alert.sound;

import com.intellij.openapi.diagnostic.Logger;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import static zd.zero.waifu.motivator.plugin.WaifuMotivator.SOUND_DIR;

public class DefaultWaifuMotivatorSoundPlayer implements WaifuMotivatorSoundPlayer {

    private static final Logger LOGGER = Logger.getInstance( DefaultWaifuMotivatorSoundPlayer.class );

    private String fileName;

    private DefaultWaifuMotivatorSoundPlayer( String fileName ) {
        this.fileName = SOUND_DIR + fileName;
    }

    public static DefaultWaifuMotivatorSoundPlayer ofFile( String fileName ) {
        return new DefaultWaifuMotivatorSoundPlayer( fileName );
    }

    @Override
    public void play() {
        playClip( Clip::start );
    }

    public void playAndWait() {
        playClip( clip -> {
            try {
                clip.start();
                Thread.sleep( clip.getMicrosecondLength() / 1000 );
            } catch ( InterruptedException e ) {
                LOGGER.error( e.getMessage(), e );
            }
        } );
    }

    private void playClip( Consumer<Clip> clipConsumer ) {
        try ( InputStream soundStream = getClass().getClassLoader().getResourceAsStream( fileName ) ) {

            if ( soundStream == null ) {
                throw new IllegalArgumentException( "Could not create stream for " + fileName );
            }
            SoundClipUtil.openClip( soundStream, clipConsumer );
        } catch ( IOException | LineUnavailableException | UnsupportedAudioFileException e ) {
            LOGGER.error( e.getMessage(), e );
        }
    }
}
