package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification

interface WaifuNotification {
    fun createNotification(): Notification
}
