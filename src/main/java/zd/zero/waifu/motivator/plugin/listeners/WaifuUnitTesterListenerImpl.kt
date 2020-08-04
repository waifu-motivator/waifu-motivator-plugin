package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.VisualMotivationAssetProvider.pickAssetFromCategories
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.VisualMotivationFactory.constructMotivation
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

internal enum class TestStatus {
    PASS, FAIL, UNKNOWN
}

class WaifuUnitTesterListenerImpl(private val project: Project) : WaifuUnitTester.Listener {
    private var lastStatus = TestStatus.UNKNOWN

    override fun onUnitTestPassed() {
        val successMotivation = constructMotivation(project,
            // todo: motivation, encouragement
            pickAssetFromCategories(
                WaifuAssetCategory.CELEBRATION,
                *getExtraTestPassCategories()
            ),
            createAlertConfiguration())
        successMotivation.motivate()
        lastStatus = TestStatus.PASS
    }

    private fun getExtraTestPassCategories(): Array<WaifuAssetCategory> =
        when (lastStatus) {
            TestStatus.FAIL -> arrayOf(WaifuAssetCategory.SMUG, WaifuAssetCategory.SMUG, WaifuAssetCategory.SMUG)
            else -> arrayOf()
        }

    override fun onUnitTestFailed() {
        val keepGoingMotivation = constructMotivation(project,
            // todo: motivation, encouragement
            pickAssetFromCategories(
                WaifuAssetCategory.DISAPPOINTMENT,
                WaifuAssetCategory.SHOCKED
            ),
            createAlertConfiguration())
        keepGoingMotivation.motivate()
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
