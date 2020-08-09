package zd.zero.waifu.motivator.plugin.listeners;

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsAdapter;
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemProcessHandler;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import zd.zero.waifu.motivator.plugin.listeners.adapter.WaifuTestPrinterAdapter;

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
                if ( testsRoot.wasTerminated() || testsRoot.isInterrupted()
                    || isCancelledOnExternalProcess( testsRoot ) ) return;
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

    /**
     * <p>A compatibility hack method to perform checking
     * for external processes (Gradle test execution)
     * to check if user has cancelled the test.</p>
     *
     * <p>For <code>202.4357.23</code>, it should be replaced with</p>
     * <p><code>
     * ExternalSystemProcessHandler external = ( ExternalSystemProcessHandler ) testsRoot.getHandler();<br>
     * ExternalSystemTaskState state = external.getTask().getState();
     * </code></p>
     *
     * @param testsRoot test execution root node
     * @return true - if it finds 'Build Cancelled'
     */
    private boolean isCancelledOnExternalProcess( @NotNull SMTestProxy.SMRootTestProxy testsRoot ) {
        if ( !( testsRoot.getHandler() instanceof ExternalSystemProcessHandler ) ) return false;

        WaifuTestPrinterAdapter printer = new WaifuTestPrinterAdapter();
        testsRoot.printOn( printer );
        testsRoot.flush();

        return printer.getPrintableContent().toLowerCase()
            .contains( "build cancelled" );
    }
}
