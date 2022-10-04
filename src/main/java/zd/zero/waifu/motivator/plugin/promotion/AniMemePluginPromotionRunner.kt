package zd.zero.waifu.motivator.plugin.promotion

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.concurrency.EdtScheduledExecutorService
import zd.zero.waifu.motivator.plugin.tools.doOrElse
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.concurrent.TimeUnit

enum class PromotionStatus {
    ACCEPTED, REJECTED, BLOCKED
}

data class PromotionResults(
    val status: PromotionStatus
)

object AniMemePromotionService {

    fun runPromotion(
        isNewUser: Boolean,
        onPromotion: (PromotionResults) -> Unit,
        onReject: () -> Unit,
    ) {
        AniMemePluginPromotionRunner(isNewUser, onPromotion, onReject)
    }
}

class AniMemePluginPromotionRunner(
    private val isNewUser: Boolean,
    private val onPromotion: (PromotionResults) -> Unit,
    private val onReject: () -> Unit
) : Runnable {

    init {
        run()
    }

    override fun run() {
        AniMemePluginPromotion.runPromotion(isNewUser, onPromotion, onReject)
    }
}

object AniMemePluginPromotion {
    fun runPromotion(
        isNewUser: Boolean,
        onPromotion: (PromotionResults) -> Unit,
        onReject: () -> Unit,
    ) {
        ApplicationManager.getApplication().executeOnPooledThread {
            // download assets on non-awt thread
            val promotionAssets = PromotionAssets(isNewUser)

            // schedule code execution to run on the EDT thread
            // so we can suggest a window
            EdtScheduledExecutorService.getInstance().schedule(
                {
                    ProjectManager.getInstance().openProjects
                        .toOptional()
                        .filter { it.isNotEmpty() }
                        .map { it.first() }
                        .map { project: Project ->
                            val window = WindowManager.getInstance().suggestParentWindow(project)
                            window?.let {
                                Pair(
                                    it,
                                    project
                                )
                            }
                        }
                        .doOrElse(
                            {
                                ApplicationManager.getApplication().invokeLater {
                                    AniMemePromotionDialog(
                                        promotionAssets,
                                        it.first,
                                        onPromotion,
                                        it.second
                                    ).show()
                                }
                            },
                            onReject
                        )
                },
                0,
                TimeUnit.SECONDS
            )
        }
    }
}
