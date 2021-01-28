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
                        val promotionAssets = PromotionAssets(isNewUser)
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
