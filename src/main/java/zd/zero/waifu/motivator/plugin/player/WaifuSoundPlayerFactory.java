package zd.zero.waifu.motivator.plugin.player;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public final class WaifuSoundPlayerFactory {

    private WaifuSoundPlayerFactory() {
        throw new AssertionError( "Never instantiate." );
    }

    public static WaifuSoundPlayer createPlayer( Path fileName ) {
        String extension = Optional.ofNullable( fileName )
            .map( Path::getFileName )
            .map( String::valueOf )
            .filter( f -> f.contains( "." ) )
            .map( f -> f.substring( f.lastIndexOf( '.' ) + 1 ) ).orElse( "" );

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

        private static Optional<SupportedFile> ofExtension( String ext ) {
            if ( StringUtils.isBlank( ext ) ) return Optional.empty();

            return Stream.of( SupportedFile.values() )
                .filter( f -> Stream.of( f.extensions ).anyMatch( x -> x.equalsIgnoreCase( ext ) ) )
                .findFirst();
        }

    }

}
