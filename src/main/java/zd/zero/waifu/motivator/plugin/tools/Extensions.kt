package zd.zero.waifu.motivator.plugin.tools

import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.util.*

fun <T> T?.toOptional() = Optional.ofNullable(this)

fun InputStream.readAllTheBytes(): ByteArray = IOUtils.toByteArray(this)
