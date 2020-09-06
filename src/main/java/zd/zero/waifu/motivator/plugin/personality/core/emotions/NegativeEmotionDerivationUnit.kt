package zd.zero.waifu.motivator.plugin.personality.core.emotions

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.tools.ProbabilityTools
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.toStream
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
        pluginState.isAllowFrustration &&
            pluginState.eventsBeforeFrustration <= observedFrustrationEvents

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
