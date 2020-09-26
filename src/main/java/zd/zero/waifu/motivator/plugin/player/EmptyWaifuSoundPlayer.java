package zd.zero.waifu.motivator.plugin.player;

import java.nio.file.Path;

public final class EmptyWaifuSoundPlayer implements WaifuSoundPlayer {

    private final Path soundFilePath;

    private EmptyWaifuSoundPlayer( Path soundFilePath ) {
        this.soundFilePath = soundFilePath;
    }

    public static WaifuSoundPlayer of( Path soundFilePath ) {
        return new EmptyWaifuSoundPlayer( soundFilePath );
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException( "Cannot play an unsupported file: '" + soundFilePath + "'" );
    }

    @Override
    public void stop() {
        play();
    }

    @Override
    public void playAndWait() {
        play();
    }
}
