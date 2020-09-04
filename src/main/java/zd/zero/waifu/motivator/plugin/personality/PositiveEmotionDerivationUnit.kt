package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import kotlin.random.Random

internal class PositiveEmotionDerivationUnit(
    private val pluginState: WaifuMotivatorState,
    private val random: Random
) : EmotionDerivationUnit {
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

    // todo: chance of smug after regular negative event
    private fun getPositiveMood(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): Mood {
        return when {
            emotionalState.mood == Mood.FRUSTRATED -> Mood.RELIEVED
            emotionalState.previousEvent?.category == MotivationEventCategory.NEGATIVE ->
                Mood.EXCITED
            else -> Mood.HAPPY
        }
    }

    private fun coolDownFrustration(emotionalState: EmotionalState): Int =
        if (emotionalState.observedNegativeEvents > 0) {
            emotionalState.observedNegativeEvents - 1
        } else {
            0
        }
}
