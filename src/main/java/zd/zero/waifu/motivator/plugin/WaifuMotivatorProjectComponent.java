package zd.zero.waifu.motivator.plugin;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginSettings;
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class WaifuMotivatorProjectComponent implements ProjectComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger( WaifuMotivatorProjectComponent.class );

    private Project project;

    private WaifuMotivatorPluginSettings settings;

    private WaifuMotivatorState pluginState;

    public WaifuMotivatorProjectComponent( Project project ) {
        this.project = project;
        this.pluginState = WaifuMotivatorPluginSettings.getInstance().getState();
    }

    @Override
    public void projectOpened() {

        if ( !pluginState.isStartupMotivationEnabled() ) return;

        NotificationGroup notificationGroup = new NotificationGroup( WaifuMotivator.PLUGIN_ID, NotificationDisplayType.BALLOON, false );
        notificationGroup.createNotification()
                .setTitle( "Mayushi" )
                .setContent( "Tuturuuu...Mayushii Desu!" )
                .notify( project );

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
