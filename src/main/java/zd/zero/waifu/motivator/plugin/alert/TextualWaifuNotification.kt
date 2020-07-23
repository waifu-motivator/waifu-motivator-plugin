package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification
import com.intellij.openapi.project.Project

class TextualWaifuNotification (
        motivationAsset: MotivationAsset,
        private val project: Project
): BaseWaifuNotification(motivationAsset) {
    override fun createNotification(): Notification {
        val notification = buildNotification()
        notification.notify(project)
        return notification
    }
}
