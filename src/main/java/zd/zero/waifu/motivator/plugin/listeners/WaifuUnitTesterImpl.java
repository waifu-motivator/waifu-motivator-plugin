package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsAdapter;
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class WaifuUnitTesterImpl implements WaifuUnitTester {

    private final MessageBusConnection busConnection;

    private final WaifuUnitTester.Listener listener;

    public WaifuUnitTesterImpl( MessageBusConnection busConnection,
                                Listener listener ) {
        this.busConnection = busConnection;
        this.listener = listener;
    }

    @Override
    public void init() {
        busConnection.subscribe( SMTRunnerEventsListener.TEST_STATUS, new SMTRunnerEventsAdapter() {
            @Override
            public void onTestingFinished( @NotNull SMTestProxy.SMRootTestProxy testsRoot ) {
                if ( testsRoot.wasTerminated() || testsRoot.isInterrupted() ) return;

                if ( testsRoot.isPassed() ) {
                    listener.onUnitTestPassed();
                } else {
                    listener.onUnitTestFailed();
                }
            }
        } );
    }

    @Override
    public void stop() {
        this.busConnection.disconnect();
    }

}
