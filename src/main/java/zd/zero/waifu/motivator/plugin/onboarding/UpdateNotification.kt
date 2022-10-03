package zd.zero.waifu.motivator.plugin.onboarding

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.BalloonLayoutData
import icons.WaifuMotivatorIcons
import org.intellij.lang.annotations.Language
import zd.zero.waifu.motivator.plugin.MessageBundle
import zd.zero.waifu.motivator.plugin.WaifuMotivator.PLUGIN_NAME
import zd.zero.waifu.motivator.plugin.onboarding.BalloonTools.fetchBalloonParameters

@Language("HTML")
val UPDATE_MESSAGE: String =
    """
      What's New?
      <br>
      <ul>
        <li>Add support for the 2022.2 build</li>
      </ul>
      <br>
      Please see the changelog for more details.
      <br><br>
    """.trimIndent()

object UpdateNotification {

    private val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup("zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification")

    fun display(
        project: Project,
        newVersion: String
    ) {
        val updateNotification = notificationGroup.createNotification(
            UPDATE_MESSAGE,
            NotificationType.INFORMATION,
        ).setTitle(MessageBundle.message("update.new.version", PLUGIN_NAME, newVersion))
            .addAction(
                NotificationAction.createSimple(MessageBundle.message("update.view.changelog")) {
                    BrowserUtil.browse("https://github.com/waifu-motivator/waifu-motivator-plugin/blob/main/CHANGELOG.md")
                }
            )

        updateNotification.icon = WaifuMotivatorIcons.MENU

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
            ) { }
            balloon.show(notificationPosition, Balloon.Position.atLeft)
        } catch (e: Throwable) {
            updateNotification.notify(project)
        }
    }
}
