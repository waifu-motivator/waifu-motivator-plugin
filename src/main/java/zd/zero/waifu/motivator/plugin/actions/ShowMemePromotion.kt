package zd.zero.waifu.motivator.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import zd.zero.waifu.motivator.plugin.promotion.AniMemePromotionService

class ShowMemePromotion: AnAction(), DumbAware {

    private var toggle = false
    override fun actionPerformed(e: AnActionEvent) {
        toggle = !toggle
        AniMemePromotionService.runPromotion(
            toggle, {}, {}
        )
    }
}
