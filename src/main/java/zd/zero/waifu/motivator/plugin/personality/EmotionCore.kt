package zd.zero.waifu.motivator.plugin.personality

import com.intellij.util.containers.concat
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.toStream
import java.util.stream.Collectors
import kotlin.random.Random

class EmotionCore(private val pluginState: WaifuMotivatorState) {

    companion object {
        private const val TOTAL_WEIGHT = 100
    }

    private val random = Random(Random(System.currentTimeMillis()).nextLong())
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
            if (observedFrustrationEvents >= pluginState.eventsBeforeFrustration) {
                tryToRemainCalm()
            } else {
                emotionalState.mood
            }

        return emotionalState.copy(
            mood = newMood,
            observedNegativeEvents = observedFrustrationEvents
        )
    }

    private val otherNegativeEmotions = listOf(
        Mood.SHOCKED, Mood.DISAPPOINTED
    )

    private fun tryToRemainCalm(): Mood {
        val weightRemaining = TOTAL_WEIGHT - pluginState.probabilityOfFrustration
        val otherWeight = weightRemaining / otherNegativeEmotions.size
        val weightedEmotions = concat(
            (Mood.FRUSTRATED to pluginState.eventsBeforeFrustration).toStream(),
            otherNegativeEmotions.stream().map { it to otherWeight }
        ).collect(Collectors.toList())
            .shuffle()
        val randomWeight = random.nextInt(0, TOTAL_WEIGHT)
        return Mood.FRUSTRATED
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
    DISAPPOINTED
}

internal data class EmotionalState(
    val mood: Mood,
    val previousEvent: MotivationEvent? = null,
    val observedPositiveEvents: Int = 0,
    val observedNeutralEvents: Int = 0,
    val observedNegativeEvents: Int = 0
)
