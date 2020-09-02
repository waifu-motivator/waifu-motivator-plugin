package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent

data class EmotionalState(
        val mood: Mood,
        val previousEvent: MotivationEvent? = null,
        val observedPositiveEvents: Int = 0,
        val observedNeutralEvents: Int = 0,
        val observedNegativeEvents: Int = 0
)
