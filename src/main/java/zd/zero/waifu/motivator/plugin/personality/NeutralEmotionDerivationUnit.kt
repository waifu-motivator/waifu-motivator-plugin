package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import kotlin.random.Random

internal class NeutralEmotionDerivationUnit(
    private val pluginState: WaifuMotivatorState,
    private val random: Random
) : EmotionDerivationUnit {

    // todo: get bored after sequential idle events
    override fun deriveEmotion(
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
