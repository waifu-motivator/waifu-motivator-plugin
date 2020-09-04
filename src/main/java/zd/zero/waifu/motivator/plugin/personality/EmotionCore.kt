package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import kotlin.random.Random

class EmotionCore(pluginState: WaifuMotivatorState) {
    private val random = Random(Random(System.currentTimeMillis()).nextLong())
    private val negativeDerivationUnit = NegativeEmotionDerivationUnit(
        pluginState,
        random
    )
    private var emotionalState = EmotionalState(Mood.CALM)

    fun updateConfig(pluginState: WaifuMotivatorState): EmotionCore {
        return EmotionCore(pluginState).let {
            it.emotionalState = this.emotionalState
            it
        }
    }

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
            MotivationEventCategory.NEGATIVE -> negativeDerivationUnit.deriveEmotion(motivationEvent, emotionalState)
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
            observedPositiveEvents = emotionalState.observedPositiveEvents + 1,
            observedNegativeEvents = coolDownFrustration(emotionalState)
        )
    }

    private fun coolDownFrustration(emotionalState: EmotionalState): Int =
        if (emotionalState.observedNegativeEvents > 0) {
            emotionalState.observedNegativeEvents - 1
        } else {
            0
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
    ENRAGED,
    FRUSTRATED,
    AGITATED,
    HAPPY,
    AMAZED,
    SMUG,
    SHOCKED,
    SURPRISED,
    CALM,
    BORED,
    DISAPPOINTED;

    override fun toString(): String {
        return this.name
    }
}

internal data class EmotionalState(
    val mood: Mood,
    val previousEvent: MotivationEvent? = null,
    val observedPositiveEvents: Int = 0,
    val observedNeutralEvents: Int = 0,
    val observedNegativeEvents: Int = 0
)
