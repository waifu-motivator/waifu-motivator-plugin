package zd.zero.waifu.motivator.plugin.personality

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Test
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.personality.core.emotions.EmotionCore
import zd.zero.waifu.motivator.plugin.personality.core.emotions.Mood
import zd.zero.waifu.motivator.plugin.personality.core.emotions.NegativeEmotionDerivationUnit
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.toList
import kotlin.random.Random

class PositiveEmotionCoreTests {

    @Test
    fun `should return smug after negative event`() {
        val mockRandom = mockk<Random>()
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = 1
                probabilityOfFrustration = 100
            },
            mockRandom
        )

        every { mockRandom.nextInt(1, 100) } returns 50
        every { mockRandom.nextInt(2) } returns 1

        listOf(
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to NegativeEmotionDerivationUnit.OTHER_NEGATIVE_EMOTIONS[1].toList(),
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.POSITIVE
            ) to Mood.SMUG.toList()
        ).forEachIndexed { index, arguments ->
            val deriveMood = emotionCore.deriveMood(
                arguments.first
            )
            Assertions.assertThat(
                deriveMood
            ).withFailMessage(
                """At index #$index
                    |${arguments.first}
                    |did not create ${arguments.second} but did $deriveMood
                """.trimMargin()
            ).isIn(arguments.second)
        }
    }
}
