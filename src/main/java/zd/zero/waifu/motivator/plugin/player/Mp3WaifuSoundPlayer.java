package zd.zero.waifu.motivator.plugin.player;

import com.intellij.openapi.diagnostic.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import static zd.zero.waifu.motivator.plugin.WaifuMotivator.SOUND_DIR;

public final class Mp3WaifuSoundPlayer implements WaifuSoundPlayer {

    private static final Logger LOGGER = Logger.getInstance( Mp3WaifuSoundPlayer.class );

    private String fileName;

    private AudioDevice audioDevice;

    private AdvancedPlayer player;

    private Mp3WaifuSoundPlayer( String fileName ) {
        this.fileName = SOUND_DIR + fileName;
    }

    public static Mp3WaifuSoundPlayer ofFile( String fileName ) {
        return new Mp3WaifuSoundPlayer( fileName );
    }

    @Override
    public void play() {
        initPlayer( l -> new Thread( l ).start() );
    }

    @Override
    public void stop() {
        if ( player == null ) {
            throw new IllegalStateException( "AdvancedPlayer must be started." );
        }

        if ( audioDevice.isOpen() ) player.close();
    }

    @Override
    public void playAndWait() {
        initPlayer( Runnable::run );
    }

    private void initPlayer( Consumer<Runnable> runnableConsumer ) {
        try ( InputStream soundStream = getClass().getClassLoader().getResourceAsStream( fileName ) ) {
            if ( soundStream == null ) {
                throw new IllegalArgumentException( "Could not create a stream for " + fileName );
            }
            audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
            player = new AdvancedPlayer( soundStream, audioDevice );

            runnableConsumer.accept( this::invokePlay );
        } catch ( IOException | JavaLayerException e ) {
            LOGGER.error( e.getMessage(), e );
        }
    }

    private void invokePlay() {
        try {
            player.play();
        } catch ( JavaLayerException e ) {
            LOGGER.error( "Cannot play sound '" + fileName + "': " + e.getMessage(), e );
        }
    }

}
