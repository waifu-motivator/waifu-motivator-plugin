package zd.zero.waifu.motivator.plugin.personality.core.emotions

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.tools.ProbabilityTools
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import java.util.stream.Stream
import kotlin.random.Random

internal class PositiveEmotionDerivationUnit(
    private val pluginState: WaifuMotivatorState,
    private val random: Random
) : EmotionDerivationUnit {

    companion object {
        val OTHER_POSITIVE_EMOTIONS = listOf(Mood.HAPPY)
    }
    private val probabilityTools = ProbabilityTools(random)

    override fun deriveEmotion(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState {
        return when {
            shouldProcessPositiveEvent(motivationEvent) ->
                processPositiveEvent(emotionalState, motivationEvent)
            else -> emotionalState
        }
    }

    override fun deriveFromMutation(
        emotionalMutationAction: EmotionalMutationAction,
        emotionalState: EmotionalState
    ): EmotionalState {
        TODO("Not yet implemented")
    }

    private fun shouldProcessPositiveEvent(motivationEvent: MotivationEvent): Boolean {
        return motivationEvent.type != MotivationEvents.IDLE
    }

    private fun processPositiveEvent(emotionalState: EmotionalState, motivationEvent: MotivationEvent): EmotionalState {
        return emotionalState.copy(
            mood = getPositiveMood(motivationEvent, emotionalState),
            observedPositiveEvents = emotionalState.observedPositiveEvents + 1,
            observedNegativeEvents = coolDownFrustration(emotionalState)
        )
    }

    private fun getPositiveMood(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): Mood {
        return when {
            emotionalState.mood == Mood.FRUSTRATED -> Mood.RELIEVED
            emotionalState.previousEvent?.category == MotivationEventCategory.NEGATIVE ->
                deriveProblemSolvedMood()
            else -> deriveStandardPositiveMood()
        }
    }

    private fun deriveStandardPositiveMood(): Mood {
        val excitedProbability = 75
        val primaryEmotions =
            Stream.of(
                Mood.EXCITED to excitedProbability
            )
        return probabilityTools.pickEmotion(
            excitedProbability,
            primaryEmotions,
            OTHER_POSITIVE_EMOTIONS
        )
    }

    private fun deriveProblemSolvedMood(): Mood {
        val primaryEmotionProbability = 80
        val excitedProbability = 20
        val primaryEmotions =
            Stream.of(
                Mood.SMUG to primaryEmotionProbability - excitedProbability,
                Mood.EXCITED to excitedProbability
            )
        return probabilityTools.pickEmotion(
            primaryEmotionProbability,
            primaryEmotions,
            OTHER_POSITIVE_EMOTIONS
        )
    }

    private fun coolDownFrustration(emotionalState: EmotionalState): Int =
        if (emotionalState.observedNegativeEvents > 0) {
            emotionalState.observedNegativeEvents - 1
        } else {
            0
        }
}
