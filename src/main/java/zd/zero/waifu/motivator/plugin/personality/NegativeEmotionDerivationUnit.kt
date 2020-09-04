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
        private const val TOTAL_WEIGHT = 100
        val OTHER_NEGATIVE_EMOTIONS = listOf(
            Mood.SHOCKED, Mood.DISAPPOINTED
        )
    }

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

        var randomWeight = random.nextInt(1, TOTAL_WEIGHT)

        for ((mood, weight) in weightedEmotions) {
            if (randomWeight <= weight) {
                return mood
            }
            randomWeight -= weight
        }

        return weightedEmotions.first { it.second > 0 }.first
    }

    private fun buildWeightedList(weightRemaining: Int, streams: Stream<Pair<Mood, Int>>): List<Pair<Mood, Int>> {
        val otherWeight = weightRemaining / OTHER_NEGATIVE_EMOTIONS.size
        return concat(
            streams,
            OTHER_NEGATIVE_EMOTIONS.stream().map { it to otherWeight }
        ).collect(Collectors.toList())
            .shuffled<Pair<Mood, Int>>()
    }
}
