package zd.zero.waifu.motivator.plugin.onboarding

import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.BalloonLayoutData
import icons.WaifuMotivatorIcons
import org.jetbrains.annotations.Nls
import zd.zero.waifu.motivator.plugin.WaifuMotivator.PLUGIN_NAME
import zd.zero.waifu.motivator.plugin.onboarding.BalloonTools.fetchBalloonParameters

val UPDATE_MESSAGE: String =
    """
      What's New?<br>
      <ul>
        <li>Waifu alert with context</li>
        <li>Preferred Waifus!</li>
        <li>Improved mood behavior</li>
      </ul>
      <br>Please see the <a href="https://github.com/waifu-motivator/waifu-motivator-plugin/blob/master/docs/CHANGELOG.md">changelog</a> for more details.
      <br><br>
      Is your Waifu missing?<br>
      Make a request for her to be featured in the <a href="https://github.com/waifu-motivator/waifu-motivator-plugin/projects/3">Waifu of the Day!</a>
      <br><br>
      Want more of your Waifu?<br>
      Make a request for <a href="https://github.com/waifu-motivator/waifu-motivator-plugin/projects/2">more assets of your Waifu!</a>
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
        @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
        @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
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
                // todo: replace with created disposable.
                Disposable {  },
            )
            balloon.show(notificationPosition, Balloon.Position.atLeft)
        } catch (e: Throwable) {
            updateNotification.notify(project)
        }
    }
}
