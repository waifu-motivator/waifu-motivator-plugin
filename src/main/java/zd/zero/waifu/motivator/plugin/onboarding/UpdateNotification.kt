package zd.zero.waifu.motivator.plugin.onboarding

import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.BalloonLayoutData
import icons.WaifuMotivatorIcons
import org.intellij.lang.annotations.Language
import org.jetbrains.annotations.Nls
import zd.zero.waifu.motivator.plugin.WaifuMotivator.PLUGIN_NAME
import zd.zero.waifu.motivator.plugin.onboarding.BalloonTools.fetchBalloonParameters

@Language("HTML")
val UPDATE_MESSAGE: String =
    """
      What's New?<br>
      <ul>
        <li>Add support for the 2021.2 build</li>
        <li>Fixed new user on-boarding issues in the 2021.1 build</li>
      </ul>
      <br>Please see the <a href="https://github.com/waifu-motivator/waifu-motivator-plugin/blob/main/CHANGELOG.md">changelog</a> for more details.
      <br><br>
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
            NotificationType.INFORMATION,
        )
            .setListener(NotificationListener.UrlOpeningListener(false))

        updateNotification.icon = WaifuMotivatorIcons.MENU

        showNotification(project, updateNotification)
    }

    fun sendMessage(
        title: String,
        message: String,
        project: Project? = null
    ) {
        showRegularNotification(
            title,
            message,
            project = project,
            listener = defaultListener
        )
    }

    private val defaultListener = NotificationListener.UrlOpeningListener(false)
    private fun showRegularNotification(
        @Nls title: String = "",
        @Nls content: String,
        project: Project? = null,
        listener: NotificationListener? = defaultListener
    ) {
        notificationGroup.createNotification(
            title,
            content,
            listener = listener
        ).setIcon(WaifuMotivatorIcons.MENU)
            .notify(project)
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
