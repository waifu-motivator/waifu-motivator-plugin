package zd.zero.waifu.motivator.plugin.tools

import java.util.*
import java.util.function.Consumer

fun <T, U, S> allOf(
    o1: Optional<T>,
    o2: Optional<U>,
    o3: Optional<S>
): Optional<Triple<T, U, S>> =
    o1.flatMap { t ->
        o2.flatMap { u ->
            o3.map { s ->
                Triple(t, u, s)
            }
        }
    }

object ToolBox {

    // todo: can be replaced by java 11 apis
    @JvmStatic
    fun <T> doOrElse(maybeOptional: Optional<T>, ifPresent: Consumer<T>, orElse: Runnable) {
        maybeOptional.doOrElse({ ifPresent.accept(it) }) { orElse.run() }
    }
}
