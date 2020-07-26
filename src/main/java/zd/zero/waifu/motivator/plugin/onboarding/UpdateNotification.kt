package zd.zero.waifu.motivator.plugin.onboarding

import com.intellij.notification.*
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.BalloonLayoutData
import zd.zero.waifu.motivator.plugin.WaifuMotivator.PLUGIN_NAME
import zd.zero.waifu.motivator.plugin.onboarding.BalloonTools.fetchBalloonParameters
import zd.zero.waifu.motivator.plugin.service.ApplicationService

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
        <li>Visual Test Pass Notifications!</li>
        <li>More settings customization.</li>
      </ul>
      <br>Please see the <a href="https://github.com/zd-zero/waifu-motivator-plugin/blob/master/CHANGELOG.md">changelog</a> for more details.
      <br><br>
      Is your Waifu missing?<br>
      Make a request for her to be featured in the <a href="https://github.com/zd-zero/waifu-motivator-plugin/projects/3">Waifu of the Day!</a>
      <br><br>
      Want more of your Waifu?<br>
      Make a request for <a href="https://github.com/zd-zero/waifu-motivator-plugin/projects/3">more assets of your Waifu!</a>
""".trimIndent()

object UpdateNotification {

    private const val UPDATE_CHANNEL_NAME = "$PLUGIN_NAME Updates"
    private val notificationGroup = NotificationGroup(
        UPDATE_CHANNEL_NAME,
        NotificationDisplayType.STICKY_BALLOON,
        false,
        UPDATE_CHANNEL_NAME
    )

    fun display(
        project: Project,
        newVersion: String
    ) {
        val updateNotification = notificationGroup.createNotification(
            "$PLUGIN_NAME updated to v$newVersion",
            UPDATE_MESSAGE,
            NotificationType.INFORMATION
        )
            .setListener(NotificationListener.UrlOpeningListener(false))

        showNotification(project, updateNotification)
    }

    private fun showNotification(
        project: Project,
        updateNotification: Notification
    ) {
        try {
            val (ideFrame, notificationPosition) = fetchBalloonParameters(project)
            val balloon = NotificationsManagerImpl.createBalloon(
                ideFrame,
                updateNotification,
                true,
                false,
                BalloonLayoutData.fullContent(),
                ApplicationService.instance
            )
            balloon.show(notificationPosition, Balloon.Position.atLeft)
        } catch (e: Throwable) {
            updateNotification.notify(project)
        }
    }
}
