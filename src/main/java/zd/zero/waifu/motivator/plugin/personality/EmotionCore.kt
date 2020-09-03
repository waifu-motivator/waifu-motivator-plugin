package zd.zero.waifu.motivator.plugin.personality

import com.intellij.util.containers.concat
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.toStream
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.random.Random

class EmotionCore(private val pluginState: WaifuMotivatorState) {

    companion object {
        private const val TOTAL_WEIGHT = 100
        val OTHER_NEGATIVE_EMOTIONS = listOf(
            Mood.SHOCKED, Mood.DISAPPOINTED
        )
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

    private fun deriveNegative(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState {
        val observedFrustrationEvents = emotionalState.observedNegativeEvents
        val newMood =
            when {
                shouldBeEnraged(observedFrustrationEvents) ->
                    hurryFindCover()

                shouldBeFrustrated(observedFrustrationEvents) ->
                    tryToRemainCalm()

                // todo: more appropriate choice based off of previous state
                else -> OTHER_NEGATIVE_EMOTIONS.random(random)
            }

        return emotionalState.copy(
            mood = newMood,
            observedNegativeEvents = observedFrustrationEvents + 1
        )
    }

    private fun shouldBeFrustrated(observedFrustrationEvents: Int) =
        pluginState.eventsBeforeFrustration in 0..observedFrustrationEvents

    private fun shouldBeEnraged(observedFrustrationEvents: Int) =
        observedFrustrationEvents >= pluginState.eventsBeforeFrustration * 2

    private fun hurryFindCover(): Mood {
        val rageProbability = pluginState.probabilityOfFrustration * 3 / 4
        val streams =
            Stream.of(
                Mood.ENRAGED to rageProbability,
                Mood.FRUSTRATED to pluginState.probabilityOfFrustration - rageProbability)

        return pickNegativeEmotion(streams)
    }

    private fun tryToRemainCalm(): Mood {
        val streams =
            (Mood.FRUSTRATED to pluginState.probabilityOfFrustration)
                .toStream()
        return pickNegativeEmotion(streams)
    }

    private fun pickNegativeEmotion(streams: Stream<Pair<Mood, Int>>): Mood {
        val weightRemaining = TOTAL_WEIGHT - pluginState.probabilityOfFrustration
        val weightedEmotions = buildWeightedList(weightRemaining, streams)

        var randomWeight = random.nextInt(0, TOTAL_WEIGHT)

        for ((mood, weight) in weightedEmotions) {
            if (randomWeight <= weight) {
                return mood
            }
            randomWeight -= weight
        }

        return weightedEmotions.first().first
    }

    private fun buildWeightedList(weightRemaining: Int, streams: Stream<Pair<Mood, Int>>): List<Pair<Mood, Int>> {
        val otherWeight = weightRemaining / OTHER_NEGATIVE_EMOTIONS.size
        return concat(
            streams,
            OTHER_NEGATIVE_EMOTIONS.stream().map { it to otherWeight }
        ).collect(Collectors.toList())
            .shuffled<Pair<Mood, Int>>()
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
