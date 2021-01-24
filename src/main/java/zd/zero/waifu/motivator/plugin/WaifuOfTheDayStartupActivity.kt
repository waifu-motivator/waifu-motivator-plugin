package zd.zero.waifu.motivator.plugin

import com.intellij.ide.GeneralSettings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.util.concurrency.EdtScheduledExecutorService
import zd.zero.waifu.motivator.plugin.alert.dialog.WaifuOfTheDayDialog
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class WaifuOfTheDayStartupActivity : StartupActivity.DumbAware {

    companion object {
        private const val IS_INITIAL_TIP_OF_THE_DAY_UPDATED = "WMP_IS_INITIAL_TIP_OF_THE_DAY_UPDATED"
    }

    override fun runActivity(project: Project) {
        updatePlatformTipOfTheDayConfig()

        // todo: use in update notification
        val disposableRef = AtomicReference<Disposable?>()
        val future = EdtScheduledExecutorService.getInstance().schedule(
            {
                val disposable = disposableRef.getAndSet(null) ?: return@schedule
                Disposer.dispose(disposable)

                if (!project.isDisposed) WaifuOfTheDayDialog.canBeShownToday(project)
            },
            0,
            TimeUnit.SECONDS
        )

        val disposable = Disposable {
            disposableRef.set(null)
            future.cancel(false)
        }

        disposableRef.set(disposable)
        Disposer.register(Disposer.newDisposable(), disposable)
    }

    private fun updatePlatformTipOfTheDayConfig() {
        val isInitialPlatformTipUpdated = PropertiesComponent.getInstance()
            .getValue(IS_INITIAL_TIP_OF_THE_DAY_UPDATED, "")
            .toBoolean()
        if (isInitialPlatformTipUpdated) return

        PropertiesComponent.getInstance().setValue(IS_INITIAL_TIP_OF_THE_DAY_UPDATED, true)
        GeneralSettings.getInstance().isShowTipsOnStartup =
            !WaifuMotivatorPluginState.getPluginState().isWaifuOfTheDayEnabled
    }
}
