package zd.zero.waifu.motivator.plugin.tools

import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.util.Optional

fun <T> T?.toOptional() = Optional.ofNullable(this)

fun InputStream.readAllTheBytes(): ByteArray = IOUtils.toByteArray(this)

// This can be removed once the plugin supports JRE 11+
fun <T> Optional<T>.doOrElse(present: (T) -> Unit, notThere: () -> Unit) =
    this.map {
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
