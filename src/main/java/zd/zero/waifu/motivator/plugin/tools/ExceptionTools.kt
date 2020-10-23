package zd.zero.waifu.motivator.plugin.tools

import com.intellij.openapi.diagnostic.Logger
import java.util.Optional

object ExceptionTools {
    private val log: Logger? = Logger.getInstance(ExceptionTools::class.java)
    fun <T> runSafely(supplier: () -> T, onError: (Throwable) -> Unit): Optional<T> {
        return try {
            Optional.ofNullable(supplier())
        } catch (throwable: Throwable) {
            if (log != null) {
                onError(throwable)
            }
            Optional.empty()
        }
    }
}
