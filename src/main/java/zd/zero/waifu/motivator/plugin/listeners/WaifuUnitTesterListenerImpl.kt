package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification.sendMessage
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.AssetTools

internal enum class TestStatus {
    PASS, FAIL, UNKNOWN
}

class WaifuUnitTesterListenerImpl(private val project: Project) : WaifuUnitTester.Listener {

    private var lastStatus = TestStatus.UNKNOWN

    override fun onUnitTestPassed() {
        AssetTools.attemptToShowCategories(
            project,
            { createAlertConfiguration() },
            {
                sendMessage(
                    "'Test Success Motivation' Unavailable Offline",
                    "Unfortunately I wasn't able to find any waifu saved locally. Please try again " +
                        "when you are back online!",
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
        AssetTools.attemptToShowCategories(
            project,
            { createAlertConfiguration() },
            {
                sendMessage(
                    "'Test Failure Motivation' Unavailable Offline",
                    "Unfortunately I wasn't able to find any waifu saved locally. Please try again " +
                        "when you are back online!",
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
