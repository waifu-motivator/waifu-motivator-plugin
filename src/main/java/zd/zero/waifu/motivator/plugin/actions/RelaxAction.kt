package zd.zero.waifu.motivator.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import zd.zero.waifu.motivator.plugin.personality.core.emotions.EMOTIONAL_MUTATION_TOPIC
import zd.zero.waifu.motivator.plugin.personality.core.emotions.EmotionalMutationAction
import zd.zero.waifu.motivator.plugin.personality.core.emotions.EmotionalMutationType.*
import zd.zero.waifu.motivator.plugin.personality.core.emotions.MoodCategory.*

class RelaxAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(EMOTIONAL_MUTATION_TOPIC)
            .onAction(EmotionalMutationAction(RESET, NEGATIVE, e.project))
    }
}
