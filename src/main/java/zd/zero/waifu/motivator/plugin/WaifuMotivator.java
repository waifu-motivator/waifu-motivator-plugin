package zd.zero.waifu.motivator.plugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class WaifuMotivator implements ProjectComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger( WaifuMotivator.class );

    private static final String WM_TITLE = "Waifu Motivator";

    private Project project;

    public WaifuMotivator( Project project ) {
        this.project = project;
    }

    @Override
    public void projectOpened() {

        Notifications.Bus.notify( new Notification(
                WM_TITLE,
                "Mayushi",
                "Tuturuuu...Mayushii Desu!",
                NotificationType.INFORMATION
        ), project );

        try ( InputStream soundStream = getClass().getClassLoader()
                .getResourceAsStream( "sound/Tuturuu...Mayushii Desu!.wav" );
              AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( soundStream ) ) {

            Clip clip = AudioSystem.getClip();
            clip.open( audioInputStream );

            if ( !project.isInitialized() ) {
                StartupManager startupManager = StartupManager.getInstance( project );
                startupManager.registerPostStartupActivity( clip::start );
            } else {
                clip.start();
            }

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
