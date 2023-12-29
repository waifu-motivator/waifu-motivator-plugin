package zd.zero.waifu.motivator.plugin.tools

import com.intellij.openapi.diagnostic.Logger

object ExceptionTools {
    private val log: Logger = Logger.getInstance(ExceptionTools::class.java)

    fun <T> runSafely(
        supplier: () -> T,
        onError: (Throwable) -> Unit,
    ): T? {
        return try {
            supplier()
        } catch (throwable: Throwable) {
            onError(throwable)
            null
        }
    }
}
