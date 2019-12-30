package zd.zero.waifu.motivator.plugin.alert.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class SoundClipUtil {
    public static void openClip( InputStream inputStream, Consumer<Clip> clipConsumer ) throws IOException,
            UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( inputStream );
        Clip clip = AudioSystem.getClip();
        clip.open( audioInputStream );

        clipConsumer.accept( clip );

        clip.addLineListener( event -> {
            if ( event.getType() == LineEvent.Type.STOP ) {
                clip.close();
            }
        } );
    }
}
