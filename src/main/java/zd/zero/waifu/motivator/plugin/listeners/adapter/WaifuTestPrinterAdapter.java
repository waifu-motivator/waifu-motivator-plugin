package zd.zero.waifu.motivator.plugin.listeners.adapter;

import com.intellij.execution.filters.HyperlinkInfo;
import com.intellij.execution.testframework.Printable;
import com.intellij.execution.testframework.Printer;
import com.intellij.execution.ui.ConsoleViewContentType;
import org.jetbrains.annotations.NotNull;

public class WaifuTestPrinterAdapter implements Printer {

    private final StringBuilder printables = new StringBuilder();

    @Override
    public void print( String text, ConsoleViewContentType contentType ) {
        if ( !ConsoleViewContentType.ERROR_OUTPUT.equals( contentType ) ) return;

        printables.append( contentType ).append( " " ).append( text );
    }

    @Override
    public void onNewAvailable( @NotNull Printable printable ) {
        // no implementation
    }

    @Override
    public void printHyperlink( String text, HyperlinkInfo info ) {
        // no implementation
    }

    @Override
    public void mark() {
        // no implementation
    }

    public String getPrintableContent() {
        return printables.toString();
    }
}
