package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification.sendMessage
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.AssetTools.attemptToShowCategories

internal enum class TestStatus {
    PASS, FAIL, UNKNOWN
}

class WaifuUnitTesterListenerImpl(private val project: Project) : WaifuUnitTester.Listener {

    private var lastStatus = TestStatus.UNKNOWN

    override fun onUnitTestPassed() {
        attemptToShowCategories(
            project,
            { createAlertConfiguration() },
            {
                sendMessage(
                    "'Test Success Motivation' Unavailable Offline",
                    ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                    project
                )
            },
            // todo: motivation, encouragement
            WaifuAssetCategory.CELEBRATION,
            *getExtraTestPassCategories()
        )

        lastStatus = TestStatus.PASS
    }

    private fun getExtraTestPassCategories(): Array<WaifuAssetCategory> =
        when (lastStatus) {
            TestStatus.FAIL -> arrayOf(WaifuAssetCategory.SMUG, WaifuAssetCategory.SMUG, WaifuAssetCategory.SMUG)
            else -> arrayOf()
        }

    override fun onUnitTestFailed() {
        attemptToShowCategories(
            project,
            { createAlertConfiguration() },
            {
                sendMessage(
                    "'Test Failure Motivation' Unavailable Offline",
                    ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                    project
                )
            },
            // todo: motivation, encouragement
            WaifuAssetCategory.DISAPPOINTMENT,
            WaifuAssetCategory.SHOCKED
        )

        lastStatus = TestStatus.FAIL
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
