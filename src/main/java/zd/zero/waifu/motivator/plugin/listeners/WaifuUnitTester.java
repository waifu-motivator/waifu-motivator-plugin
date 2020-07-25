package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.openapi.project.Project;
import zd.zero.waifu.motivator.plugin.tools.AlarmDebouncer;
import zd.zero.waifu.motivator.plugin.tools.Debouncer;

public interface WaifuUnitTester {

    static WaifuUnitTester newInstance( Project project ) {
        return new WaifuUnitTesterImpl( project.getMessageBus().connect(),
                new WaifuUnitTesterListenerImpl( project ),
            new AlarmDebouncer( Debouncer.DEFAULT_INTERVAL )
        );
    }

    void init();

    void stop();

    interface Listener {

        void onUnitTestPassed();

        void onUnitTestFailed();

    }

}
