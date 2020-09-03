package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState

class EmotionCore {

    private var emotionalState = EmotionalState(Mood.CALM)

    fun deriveMood(motivationEvent: MotivationEvent): Mood {
        emotionalState = processEvent(motivationEvent, emotionalState)
        return emotionalState.mood
    }

    private fun processEvent(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState {
        return when (motivationEvent.category) {
            MotivationEventCategory.POSITIVE -> derivePositive(motivationEvent, emotionalState)
            MotivationEventCategory.NEGATIVE -> deriveNegative(motivationEvent, emotionalState)
            MotivationEventCategory.NEUTRAL -> deriveNeutral(motivationEvent, emotionalState)
        }.copy(
            previousEvent = emotionalState.previousEvent
        )
    }

    private fun derivePositive(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState {
        return emotionalState.copy(
            observedPositiveEvents = emotionalState.observedPositiveEvents + 1
        )
    }

    private fun deriveNegative(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState {
        val observedFrustrationEvents = emotionalState.observedNegativeEvents + 1
        val newMood =
            if (observedFrustrationEvents >= WaifuMotivatorPluginState.getPluginState().eventsBeforeFrustration) {
                Mood.FRUSTRATED
            } else {
                emotionalState.mood
            }

        return emotionalState.copy(
            mood = newMood,
            observedNegativeEvents = observedFrustrationEvents
        )
    }

    private fun deriveNeutral(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState =
        when (motivationEvent.type) {
            MotivationEvents.IDLE -> EmotionalState(Mood.CALM)
            else -> emotionalState
        }.copy(
            observedNeutralEvents = emotionalState.observedNeutralEvents + 1
        )
}

enum class Mood {
    ENRAGED, FRUSTRATED, AGITATED, HAPPY, AMAZED, CALM, BORED
}

internal data class EmotionalState(
    val mood: Mood,
    val previousEvent: MotivationEvent? = null,
    val observedPositiveEvents: Int = 0,
    val observedNeutralEvents: Int = 0,
    val observedNegativeEvents: Int = 0
)
