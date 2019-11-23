package zd.zero.waifu.motivator.plugin.listeners;

public interface WaifuUnitTester {

    void init();

    void stop();

    interface Listener {

        void onUnitTestPassed();

        void onUnitTestFailed();

    }

}
