package zd.zero.waifu.motivator.plugin;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class MessageBundle extends AbstractBundle {

    public static final String BUNDLE = "messages.MessageBundle";

    private static final MessageBundle INSTANCE = new MessageBundle();

    protected MessageBundle() {
        super( BUNDLE );
    }

    public static String message( @NotNull @PropertyKey( resourceBundle = BUNDLE ) String key, @NotNull Object... params ) {
        return INSTANCE.getMessage( key, params );
    }
}
