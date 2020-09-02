package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

internal enum class TestStatus {
    PASS, FAIL, UNKNOWN
}

class WaifuUnitTesterListenerImpl(private val project: Project) : WaifuUnitTester.Listener {

    override fun onUnitTestPassed() {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(MotivationEventListener.TOPIC)
            .onEventTrigger(
                MotivationEvent(
                    MotivationEvents.TEST,
                    MotivationEventCategory.POSITIVE,
                    "Test Success Motivation",
                    project
                ) { createAlertConfiguration() }
            )
    }

    override fun onUnitTestFailed() {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(MotivationEventListener.TOPIC)
            .onEventTrigger(
                MotivationEvent(
                    MotivationEvents.TEST,
                    MotivationEventCategory.NEGATIVE,
                    "Test Failure Motivation",
                    project
                ) { createAlertConfiguration() }
            )
    }

    private fun createAlertConfiguration(): AlertConfiguration {
        val pluginState = WaifuMotivatorPluginState.getInstance().state
        return AlertConfiguration(
            pluginState!!.isUnitTesterMotivationEnabled || pluginState.isUnitTesterMotivationSoundEnabled,
            pluginState.isUnitTesterMotivationEnabled,
            pluginState.isUnitTesterMotivationSoundEnabled
        )
    }
}
