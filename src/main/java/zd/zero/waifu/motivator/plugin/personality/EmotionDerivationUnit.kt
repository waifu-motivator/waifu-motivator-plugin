package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent

internal interface EmotionDerivationUnit {
    fun deriveEmotion(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState
}
