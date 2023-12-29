package zd.zero.waifu.motivator.plugin.tools

import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.util.Optional
import java.util.stream.Stream

fun <T> T?.toOptional() = Optional.ofNullable(this)

fun InputStream.readAllTheBytes(): ByteArray = IOUtils.toByteArray(this)

inline fun <reified T> T.toArray(): Array<T> = arrayOf(this)

inline fun <reified T> T.toList(): List<T> = listOf(this)

fun <T> T.toStream(): Stream<T> = Stream.of(this)

// todo: This can be removed once the plugin supports only JRE 11+
fun <T> Optional<T>.doOrElse(
    present: (T) -> Unit,
    notThere: () -> Unit,
) = this.map {
    it to true
}.map {
    it.toOptional()
}.orElseGet {
    (null to false).toOptional()
}.ifPresent {
    if (it.second) {
        present(it.first)
    } else {
        notThere()
    }
}
