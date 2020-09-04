package zd.zero.waifu.motivator.plugin.personality

import com.intellij.util.containers.concat
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.toStream
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.random.Random

internal class NegativeEmotionDerivationUnit(
    private val pluginState: WaifuMotivatorState,
    private val random: Random
) : EmotionDerivationUnit {

    companion object {
        val OTHER_NEGATIVE_EMOTIONS = listOf(
            Mood.SHOCKED, Mood.DISAPPOINTED
        )
    }

    private val probabilityTools = ProbabilityTools(random)

    override fun deriveEmotion(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState =
        when {
            shouldProcessNegativeEvent(motivationEvent) -> processNegativeEvent(emotionalState)
            else -> emotionalState
        }

    private fun shouldProcessNegativeEvent(motivationEvent: MotivationEvent): Boolean =
        motivationEvent.type != MotivationEvents.IDLE

    private fun processNegativeEvent(emotionalState: EmotionalState): EmotionalState {
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
        shouldBeFrustrated(observedFrustrationEvents) &&
            observedFrustrationEvents >= pluginState.eventsBeforeFrustration * 2

    private fun hurryFindCover(): Mood {
        val rageProbability = pluginState.probabilityOfFrustration * 3 / 4
        val primaryEmotions =
            Stream.of(
                Mood.ENRAGED to rageProbability,
                Mood.FRUSTRATED to pluginState.probabilityOfFrustration - rageProbability)

        return pickNegativeEmotion(primaryEmotions)
    }

    private fun tryToRemainCalm(): Mood {
        val primaryEmotions =
            (Mood.FRUSTRATED to pluginState.probabilityOfFrustration)
                .toStream()
        return pickNegativeEmotion(primaryEmotions)
    }

    private fun pickNegativeEmotion(
        primaryEmotions: Stream<Pair<Mood, Int>>
    ): Mood {
        val secondaryEmotions = OTHER_NEGATIVE_EMOTIONS
        val probabilityOfPrimaryEmotions = pluginState.probabilityOfFrustration
        return probabilityTools.pickEmotion(probabilityOfPrimaryEmotions, primaryEmotions, secondaryEmotions)
    }
}

class ProbabilityTools(
    private val random: Random
) {
    companion object {
        private const val TOTAL_WEIGHT = 100
    }

    fun pickEmotion(
        probabilityOfPrimaryEmotions: Int,
        primaryEmotions: Stream<Pair<Mood, Int>>,
        secondaryEmotions: List<Mood>
    ): Mood {
        assert(probabilityOfPrimaryEmotions in 0..TOTAL_WEIGHT) { "Expected probability to be from 0 to 100" }
        val weightRemaining = TOTAL_WEIGHT - probabilityOfPrimaryEmotions
        val weightedEmotions = buildWeightedList(
            weightRemaining,
            primaryEmotions,
            secondaryEmotions
        )
        return pickFromWeightedList(
            random.nextInt(1, TOTAL_WEIGHT),
            weightedEmotions
        )
    }

    private fun buildWeightedList(
        weightRemaining: Int,
        primaryEmotions: Stream<Pair<Mood, Int>>,
        secondaryEmotions: List<Mood>
    ): List<Pair<Mood, Int>> {
        val secondaryEmotionWeights = weightRemaining / secondaryEmotions.size
        return concat(
            primaryEmotions,
            secondaryEmotions.stream().map { it to secondaryEmotionWeights }
        ).collect(Collectors.toList())
            .shuffled<Pair<Mood, Int>>()
    }

    private fun pickFromWeightedList(
        weightChosen: Int,
        weightedEmotions: List<Pair<Mood, Int>>
    ): Mood {
        var randomWeight = weightChosen
        for ((mood, weight) in weightedEmotions) {
            if (randomWeight <= weight) {
                return mood
            }
            randomWeight -= weight
        }

        return weightedEmotions.first { it.second > 0 }.first
    }
}
