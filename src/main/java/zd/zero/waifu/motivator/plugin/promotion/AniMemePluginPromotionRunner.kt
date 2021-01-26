package zd.zero.waifu.motivator.plugin.promotion

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import zd.zero.waifu.motivator.plugin.tools.doOrElse
import zd.zero.waifu.motivator.plugin.tools.toOptional

enum class PromotionStatus {
    ACCEPTED, REJECTED, BLOCKED
}

data class PromotionResults(
    val status: PromotionStatus
)

object AniMemePromotionService {

    fun runPromotion(
        onPromotion: (PromotionResults) -> Unit,
        onReject: () -> Unit,
    ) {
        AniMemePluginPromotionRunner(onPromotion, onReject)
    }
}

class AniMemePluginPromotionRunner(
    private val onPromotion: (PromotionResults) -> Unit,
    private val onReject: () -> Unit
) : Runnable {

    init {
        run()
    }

    override fun run() {
        AniMemePluginPromotion.runPromotion(onPromotion, onReject)
    }
}

object AniMemePluginPromotion {
    fun runPromotion(
        onPromotion: (PromotionResults) -> Unit,
        onReject: () -> Unit,
    ) {
        ApplicationManager.getApplication().executeOnPooledThread {
            ProjectManager.getInstance().openProjects
                .toOptional()
                .filter { it.isNotEmpty() }
                .map { it.first() }
                .map {
                    WindowManager.getInstance().suggestParentWindow(
                        it
                    )
                }
                .doOrElse(
                    {
                        val promotionAssets = PromotionAssets()
                        ApplicationManager.getApplication().invokeLater {
                            AniMemePromotionDialog(
                                promotionAssets,
                                it!!,
                                onPromotion
                            ).show()
                        }
                    },
                    onReject
                )
        }
    }
}
