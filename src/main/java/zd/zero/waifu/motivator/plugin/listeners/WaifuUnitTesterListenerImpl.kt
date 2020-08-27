package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider.pickAssetFromCategories
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory.constructMotivation
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification.sendMessage
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.doOrElse

internal enum class TestStatus {
    PASS, FAIL, UNKNOWN
}

class WaifuUnitTesterListenerImpl(private val project: Project) : WaifuUnitTester.Listener {
    companion object {
        private const val MAXIMUM_RETRY_ATTEMPTS = 6
    }
    private var lastStatus = TestStatus.UNKNOWN

    override fun onUnitTestPassed() {
        // todo: motivation, encouragement
        attemptToDisplayNotification(
            0,
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
        // todo: motivation, encouragement
        attemptToDisplayNotification(
            0,
            WaifuAssetCategory.DISAPPOINTMENT,
            WaifuAssetCategory.SHOCKED
        )

        lastStatus = TestStatus.FAIL
    }

    private fun attemptToDisplayNotification(
        attempts: Int,
        vararg categories: WaifuAssetCategory
    ) {
        if (attempts < MAXIMUM_RETRY_ATTEMPTS) {
            pickAssetFromCategories(
                *categories
            ).doOrElse({ asset ->
                constructMotivation(project,
                    asset,
                    createAlertConfiguration()).motivate()
            }) {
                attemptToDisplayNotification(attempts + 1, * categories)
            }
        } else {
            sendMessage(
                "'Test Motivation' Unavailable Offline",
                "Unfortunately I wasn't able to find any waifu saved locally. Please try again " +
                    "when you are back online!",
                project
            )
        }
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
