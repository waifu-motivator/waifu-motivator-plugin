package zd.zero.waifu.motivator.plugin.player;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class SoundClipUtil {
    public static Clip openClip( InputStream inputStream ) throws IOException,
            UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( inputStream );
        Clip clip = AudioSystem.getClip();
        clip.open( audioInputStream );
        clip.addLineListener( event -> {
            if ( event.getType() == LineEvent.Type.STOP ) {
                clip.close();
            }
        } );

        return clip;
    }
}
