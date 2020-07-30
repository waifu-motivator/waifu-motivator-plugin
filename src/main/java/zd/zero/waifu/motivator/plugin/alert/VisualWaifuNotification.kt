package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.BalloonLayoutData
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset
import zd.zero.waifu.motivator.plugin.onboarding.BalloonTools.fetchBalloonParameters
import zd.zero.waifu.motivator.plugin.service.ApplicationService

class VisualWaifuNotification(
    motivationAsset: MotivationAsset,
    val project: Project
) : BaseWaifuNotification(motivationAsset) {

    override fun createNotification(): Notification {
        val updateNotification = buildNotification()
        try {
            val (ideFrame, notificationPosition) = fetchBalloonParameters(project)
            val balloon = NotificationsManagerImpl.createBalloon(
                ideFrame,
                updateNotification,
                true,
                true,
                BalloonLayoutData.fullContent(),
                ApplicationService.instance
            )
            balloon.setAnimationEnabled(true)
            balloon.show(notificationPosition, Balloon.Position.atLeft)
        } catch (e: Throwable) {
            updateNotification.notify(project)
        }

        return updateNotification
    }
}
