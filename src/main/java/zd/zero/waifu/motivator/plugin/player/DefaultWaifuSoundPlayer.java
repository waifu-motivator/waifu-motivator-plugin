package zd.zero.waifu.motivator.plugin.player;

import com.intellij.openapi.diagnostic.Logger;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DefaultWaifuSoundPlayer implements WaifuSoundPlayer {

    private static final Logger LOGGER = Logger.getInstance( DefaultWaifuSoundPlayer.class );

    private final Path filePath;

    private Clip clip;

    private DefaultWaifuSoundPlayer( Path filePath ) {
        this.filePath = filePath;
    }

    public static DefaultWaifuSoundPlayer ofFile( Path filePath ) {
        return new DefaultWaifuSoundPlayer( filePath );
    }

    @Override
    public void play() {
        try ( InputStream soundStream = new BufferedInputStream(Files.newInputStream( filePath )) ) {
            clip = SoundClipUtil.openClip( soundStream );
            clip.start();
        } catch ( IOException | LineUnavailableException | UnsupportedAudioFileException e ) {
            LOGGER.warn( e.getMessage(), e );
        }
    }

    public void playAndWait() {
        play();
        try {
            Thread.sleep( clip.getMicrosecondLength() / 1000 );
        } catch ( InterruptedException e ) {
            LOGGER.warn( e.getMessage(), e );
        }
    }

    @Override
    public void stop() {
        if ( clip == null ) {
            LOGGER.warn( "Unable to stop clip because it was never started" );
            return;
        }
        clip.stop();
    }
}
