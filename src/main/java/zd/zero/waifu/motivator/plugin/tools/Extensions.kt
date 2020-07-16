package zd.zero.waifu.motivator.plugin.tools

import java.util.*

fun <T> T?.toOptional() = Optional.ofNullable(this)