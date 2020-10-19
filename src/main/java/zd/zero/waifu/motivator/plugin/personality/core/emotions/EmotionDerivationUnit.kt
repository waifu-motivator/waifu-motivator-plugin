package zd.zero.waifu.motivator.plugin.personality.core.emotions

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent

internal interface EmotionDerivationUnit {
    fun deriveEmotion(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState

    fun deriveFromMutation(
        emotionalMutationAction: EmotionalMutationAction,
        emotionalState: EmotionalState
    ): EmotionalState
}
