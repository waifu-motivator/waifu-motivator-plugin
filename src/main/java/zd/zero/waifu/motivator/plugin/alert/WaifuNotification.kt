package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification

fun interface WaifuNotification {
    fun createNotification(): Notification
}
