package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import zd.zero.waifu.motivator.plugin.WaifuMotivator

abstract class BaseNotification(
    private val motivationAsset: MotivationAsset
): WaifuNotification {

    protected fun buildNotification(): Notification {
        val notificationGroup = NotificationGroup(
                WaifuMotivator.PLUGIN_NAME, NotificationDisplayType.BALLOON, false
        )
        return notificationGroup.createNotification()
            .setTitle(motivationAsset.title.ifEmpty { "" })
            .setContent(motivationAsset.message.ifEmpty { "" })
    }

}
