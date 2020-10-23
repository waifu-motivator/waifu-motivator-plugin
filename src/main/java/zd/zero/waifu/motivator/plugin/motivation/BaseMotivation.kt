package zd.zero.waifu.motivator.plugin.motivation

import com.intellij.ide.ui.UISettings
import com.intellij.notification.Notification
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.util.registry.Registry
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.alert.WaifuNotification
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayer
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.toOptional

abstract class BaseMotivation(
    private val notifier: WaifuNotification,
    private val player: WaifuSoundPlayer,
    private val config: AlertConfiguration
) : WaifuMotivation {

    companion object {
        private const val KEY_DISTRACTION_FREE_MODE = "editor.distraction.free.mode"
    }

    private lateinit var listener: MotivationListener

    override fun motivate() {
        if (isAlertEnabled && isDistractionAllowed) {
            if (isDisplayNotificationEnabled) {
                displayNotification()
            } else {
                notifyDisposal()
            }
            if (isSoundAlertEnabled) soundAlert()
        } else {
            notifyDisposal()
        }
    }

    private fun notifyDisposal() {
        if (this::listener.isInitialized) {
            listener.onDisposal()
        }
    }

    override fun displayNotification() {
        val notification = notifier.createNotification()
        notification.balloon.toOptional().ifPresent {
            it.addListener(
                object : JBPopupListener {
                    override fun onClosed(event: LightweightWindowEvent) {
                        onAlertClosed(notification)
                    }
                }
            )
        }
    }

    override fun isDistractionAllowed(): Boolean =
        !(
            WaifuMotivatorPluginState.getInstance().state?.isDisabledInDistractionFreeMode ?: true &&
                (
                    Registry.get(KEY_DISTRACTION_FREE_MODE).asBoolean() ||
                        UISettings.instance.presentationMode
                    )
            )

    override fun onAlertClosed(notification: Notification) {
        val duration = System.currentTimeMillis() - notification.timestamp
        val isExpired = duration / 1000 >= 10
        if (!isExpired && config.isSoundAlertEnabled) {
            player.stop()
        }

        notifyDisposal()
    }

    override fun setListener(motivationListener: MotivationListener): WaifuMotivation {
        this.listener = motivationListener
        return this
    }

    override fun isAlertEnabled(): Boolean =
        config.isAlertEnabled

    override fun isDisplayNotificationEnabled(): Boolean =
        config.isDisplayNotificationEnabled

    override fun soundAlert() {
        player.play()
    }

    override fun isSoundAlertEnabled(): Boolean =
        config.isSoundAlertEnabled
}
