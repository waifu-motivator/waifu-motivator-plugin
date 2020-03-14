package zd.zero.waifu.motivator.plugin.player;

import org.apache.commons.io.FilenameUtils;

import java.util.Optional;
import java.util.stream.Stream;

public final class WaifuSoundPlayerFactory {

    private WaifuSoundPlayerFactory() {
        throw new AssertionError( "Never instantiate." );
    }

    public static WaifuSoundPlayer createPlayer( String fileName ) {
        String extension = FilenameUtils.getExtension( fileName );
        Optional<SupportedFile> supportedFile = SupportedFile.ofExtension( extension );
        if ( supportedFile.isPresent() ) {
            switch ( supportedFile.get() ) {
                case CLIP:
                    return DefaultWaifuSoundPlayer.ofFile( fileName );

                case MP3:
                    return Mp3WaifuSoundPlayer.ofFile( fileName );

            }
        }

        return EmptyWaifuSoundPlayer.of( fileName );
    }

    private enum SupportedFile {

        CLIP( "wav", "au" ),

        MP3( "mp3" );

        private final String[] extensions;

        SupportedFile( String... extensions ) {
            this.extensions = extensions;
        }

        public String[] getExtensions() {
            return extensions;
        }

        private static Optional<SupportedFile> ofExtension( String ext ) {
            return Stream.of( SupportedFile.values() )
                    .filter( f -> Stream.of( f.getExtensions() ).anyMatch( x -> x.equalsIgnoreCase( ext ) ) )
                    .findFirst();
        }

    }

}
