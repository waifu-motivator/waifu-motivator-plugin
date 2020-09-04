package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import kotlin.random.Random

class EmotionCore(
    pluginState: WaifuMotivatorState,
    private val random: Random = Random(Random(System.currentTimeMillis()).nextLong())
) {
    private val negativeDerivationUnit = NegativeEmotionDerivationUnit(
        pluginState, random
    )
    private val positiveDerivationUnit = PositiveEmotionDerivationUnit(
        pluginState, random
    )
    private val neutralDerivationUnit = NeutralEmotionDerivationUnit(
        pluginState, random
    )
    private var emotionalState = EmotionalState(Mood.CALM)

    fun updateConfig(pluginState: WaifuMotivatorState): EmotionCore =
        EmotionCore(pluginState, random).let {
            it.emotionalState = this.emotionalState
            it
        }

    fun deriveMood(motivationEvent: MotivationEvent): Mood {
        emotionalState = processEvent(motivationEvent, emotionalState)
        return emotionalState.mood
    }

    private fun processEvent(
        motivationEvent: MotivationEvent,
        emotionalState: EmotionalState
    ): EmotionalState =
        when (motivationEvent.category) {
            MotivationEventCategory.POSITIVE -> positiveDerivationUnit.deriveEmotion(motivationEvent, emotionalState)
            MotivationEventCategory.NEGATIVE -> negativeDerivationUnit.deriveEmotion(motivationEvent, emotionalState)
            MotivationEventCategory.NEUTRAL -> neutralDerivationUnit.deriveEmotion(motivationEvent, emotionalState)
        }.copy(
            previousEvent = emotionalState.previousEvent
        )
}

enum class Mood {
    ENRAGED,
    FRUSTRATED,
    AGITATED,
    HAPPY,
    RELIEVED,
    EXCITED,
    PROUD,
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
