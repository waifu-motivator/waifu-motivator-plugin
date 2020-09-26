package zd.zero.waifu.motivator.plugin.alert

import com.intellij.notification.Notification
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.util.Ref
import com.intellij.ui.BalloonLayoutData
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset
import zd.zero.waifu.motivator.plugin.onboarding.BalloonTools.fetchBalloonParameters
import zd.zero.waifu.motivator.plugin.service.ApplicationService

open class VisualWaifuNotification(
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
                createLayoutDataRef(),
                ApplicationService.instance
            )
            balloon.setAnimationEnabled(true)
            balloon.show(notificationPosition, Balloon.Position.atLeft)
        } catch (e: Throwable) {
            updateNotification.notify(project)
        }

        return updateNotification
    }

    private fun createLayoutDataRef(): Ref<BalloonLayoutData> {
        val balloonLayoutData = BalloonLayoutData.createEmpty()
        balloonLayoutData.showFullContent = true
        balloonLayoutData.showMinSize = false
        balloonLayoutData.welcomeScreen = true
        return Ref(balloonLayoutData)
    }
}
