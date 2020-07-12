package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.openapi.project.Project;

public interface WaifuUnitTester {

    static WaifuUnitTester newInstance( Project project ) {
        return new WaifuUnitTesterImpl( project.getMessageBus().connect(),
                new WaifuUnitTesterListenerImpl( project ) );
    }

    void init();

    void stop();

    interface Listener {

        void onUnitTestPassed();

        void onUnitTestFailed();

    }

}
