package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.BalloonLayoutData
import zd.zero.waifu.motivator.plugin.onboarding.fetchBalloonParameters

class VisualWaifuNotification(
        motivationAsset: MotivationAsset,
        val project: Project
) : BaseNotification(motivationAsset) {

    override fun createNotification(): Notification {
        val updateNotification = buildNotification()
        try {
            val (ideFrame, notificationPosition) = fetchBalloonParameters(project)
            val balloon = NotificationsManagerImpl.createBalloon(
                ideFrame,
                updateNotification,
                true,
                false,
                    BalloonLayoutData.fullContent()
            ) {}
            balloon.show(notificationPosition, Balloon.Position.atLeft)
        } catch (e: Throwable) {
            updateNotification.notify(project)
        }

        return updateNotification
    }
}
