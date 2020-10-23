package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification
import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset

class NonTitledVisualNotification(motivationAsset: MotivationAsset, project: Project) :
    VisualWaifuNotification(motivationAsset, project) {

    override fun buildNotification(): Notification =
        notificationGroup.createNotification()
            .setContent(motivationAsset.message.ifEmpty { "" })
}
