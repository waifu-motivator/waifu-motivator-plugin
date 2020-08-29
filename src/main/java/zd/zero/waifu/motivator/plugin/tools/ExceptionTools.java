package zd.zero.waifu.motivator.plugin.tools;


import com.intellij.openapi.diagnostic.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExceptionTools {
    private static Logger log = Logger.getInstance( ExceptionTools.class );

    public static <T> Optional<T> runSafely( Supplier<T> supplier, Consumer<Throwable> onError ) {
        try {
            return Optional.ofNullable( supplier.get() );
        } catch ( Throwable throwable ) {
            if ( log != null ) {
                onError.accept( throwable );
            }
            return Optional.empty();
        }
    }
}
