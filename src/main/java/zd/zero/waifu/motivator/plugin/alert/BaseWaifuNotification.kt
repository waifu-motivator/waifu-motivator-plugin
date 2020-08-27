package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import zd.zero.waifu.motivator.plugin.WaifuMotivator
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset

abstract class BaseWaifuNotification(
    protected val motivationAsset: MotivationAsset
) : WaifuNotification {

    companion object {
        val notificationGroup = NotificationGroup(
            WaifuMotivator.PLUGIN_NAME, NotificationDisplayType.BALLOON, false
        )
    }

    protected open fun buildNotification(): Notification {
        return notificationGroup.createNotification()
            .setTitle(motivationAsset.title.ifEmpty { "" })
            .setContent(motivationAsset.message.ifEmpty { "" })
    }
}
