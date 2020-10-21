package zd.zero.waifu.motivator.plugin.integrations

import com.intellij.icons.AllIcons
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Consumer
import zd.zero.waifu.motivator.plugin.personality.Wendi
import zd.zero.waifu.motivator.plugin.personality.core.emotions.EMOTION_TOPIC
import zd.zero.waifu.motivator.plugin.personality.core.emotions.Mood
import zd.zero.waifu.motivator.plugin.personality.core.emotions.MoodListener
import zd.zero.waifu.motivator.plugin.settings.PluginSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorSettingsPage
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.awt.event.MouseEvent
import javax.swing.Icon

class MoodStatusBarWidget(private val project: Project) :
    StatusBarWidget,
    StatusBarWidget.IconPresentation {
    companion object {
        private const val ID = "Waifu Mood Status Component"
    }

    private val connect = ApplicationManager.getApplication().messageBus.connect()

    init {
        connect.subscribe(LafManagerListener.TOPIC, LafManagerListener {
            updateWidget()
        })
        connect.subscribe(PluginSettingsListener.PLUGIN_SETTINGS_TOPIC, object : PluginSettingsListener {
            override fun settingsUpdated(newPluginState: WaifuMotivatorState) {
                updateWidget()
            }
        })
        connect.subscribe(EMOTION_TOPIC, object : MoodListener {
            override fun onDerivedMood(currentMood: Mood) {
                updateWidget()
            }
        })
        StartupManager.getInstance(project).runWhenProjectIsInitialized { updateWidget() }
    }

    private fun updateWidget() {
        WindowManager.getInstance().getStatusBar(project).toOptional()
            .ifPresent {
                it.updateWidget(ID)
            }
    }

    override fun getTooltipText(): String = Wendi.currentMood
        .filter { WaifuMotivatorPluginState.getPluginState().showMood }
        .map { it.toString() }
        .orElse(null)

    override fun ID(): String = ID

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun install(statusBar: StatusBar) {
        statusBar.updateWidget(ID)
    }

    override fun dispose() {
    }

    override fun getIcon(): Icon? =
        Wendi.currentMood
            .filter { WaifuMotivatorPluginState.getPluginState().showMood }
            .map { AllIcons.Ide.Rating }
            .orElse(null)

    override fun getClickConsumer(): Consumer<MouseEvent> = Consumer {
        ApplicationManager.getApplication().invokeLater({
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project,
                WaifuMotivatorSettingsPage::class.java
            )
        }, ModalityState.NON_MODAL)
    }
}
