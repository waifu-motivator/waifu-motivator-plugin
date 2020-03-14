package zd.zero.waifu.motivator.plugin.player;

public final class EmptyWaifuSoundPlayer implements WaifuSoundPlayer {

    private String fileName;

    private EmptyWaifuSoundPlayer( String fileName ) {
        this.fileName = fileName;
    }

    public static WaifuSoundPlayer of( String fileName ) {
        return new EmptyWaifuSoundPlayer( fileName );
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException( "Cannot play an unsupported file: '" + fileName + "'" );
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
