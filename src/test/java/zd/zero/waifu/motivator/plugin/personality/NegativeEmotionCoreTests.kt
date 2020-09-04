package zd.zero.waifu.motivator.plugin.personality

import com.intellij.openapi.project.Project
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Test
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.personality.NegativeEmotionDerivationUnit.Companion.OTHER_NEGATIVE_EMOTIONS
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.toList
import kotlin.random.Random

class NegativeEmotionCoreTests {

    @Test
    fun deriveMoodShouldReturnCalmAfterIdleEvent() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState()
        )

        listOf(
            buildMotivationEvent(
                MotivationEvents.IDLE,
                MotivationEventCategory.NEUTRAL
            ),

            buildMotivationEvent(
                MotivationEvents.IDLE,
                MotivationEventCategory.NEGATIVE
            ),

            buildMotivationEvent(
                MotivationEvents.IDLE,
                MotivationEventCategory.POSITIVE
            )
        ).forEachIndexed { index, event ->
            val deriveMood = emotionCore.deriveMood(
                event
            )
            val expectedMood = Mood.CALM
            Assertions.assertThat(
                deriveMood
            ).withFailMessage(
                """At index #$index
                    |$event
                    |did not create $expectedMood but did $deriveMood
                """.trimMargin()
            ).isEqualTo(expectedMood)
        }
    }

    @Test
    fun deriveMoodShouldAlwaysReturnFrustratedAfterExpectedEvents() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = 4
                probabilityOfFrustration = 100
            }
        )

        val negativeEmotions =
            OTHER_NEGATIVE_EMOTIONS
        val calm = Mood.CALM.toList()
        val frustrated = Mood.FRUSTRATED.toList()
        listOf(
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated
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

    @Test
    fun shouldCalmDownAfterIdleEvent() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = 1
                probabilityOfFrustration = 100
            }
        )

        val negativeEmotions =
            OTHER_NEGATIVE_EMOTIONS
        val calm = Mood.CALM.toList()
        val frustrated = Mood.FRUSTRATED.toList()
        listOf(
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated,
            buildMotivationEvent(
                MotivationEvents.IDLE,
                MotivationEventCategory.NEUTRAL
            ) to calm,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to listOf(Mood.FRUSTRATED, Mood.ENRAGED)
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

    @Test
    fun deriveMoodShouldReturnNeverReturnFrustratedWhenEventsIsNegative() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = -1
                probabilityOfFrustration = 100
            }
        )

        val motivationEvent = buildMotivationEvent(
            MotivationEvents.TASK,
            MotivationEventCategory.NEGATIVE
        )

        val negativeEmotions =
            OTHER_NEGATIVE_EMOTIONS.toTypedArray()
        repeat(42) { index ->
            val deriveMood = emotionCore.deriveMood(
                motivationEvent
            )
            Assertions.assertThat(
                deriveMood
            ).withFailMessage(
                """At index #$index
                    |$motivationEvent
                    |did not create $OTHER_NEGATIVE_EMOTIONS but did $deriveMood
                """.trimMargin()
            ).isIn(
                *negativeEmotions
            )
        }
    }

    @Test
    fun deriveMoodShouldReturnNeverReturnFrustratedWhenProbabilityIsZero() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = 0
                probabilityOfFrustration = 0
            }
        )

        val motivationEvent = buildMotivationEvent(
            MotivationEvents.TASK,
            MotivationEventCategory.NEGATIVE
        )

        val negativeEmotions =
            OTHER_NEGATIVE_EMOTIONS.toTypedArray()
        repeat(42) { index ->
            val deriveMood = emotionCore.deriveMood(
                motivationEvent
            )
            Assertions.assertThat(
                deriveMood
            ).withFailMessage(
                """At index #$index
                    |$motivationEvent
                    |did not create $OTHER_NEGATIVE_EMOTIONS but did $deriveMood
                """.trimMargin()
            ).isIn(
                *negativeEmotions
            )
        }
    }

    @Test
    fun `frustration should evolve into rage`() {
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

        val frustrated = Mood.FRUSTRATED.toList()
        listOf(
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to OTHER_NEGATIVE_EMOTIONS[1].toList(),
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.ENRAGED.toList(),
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.ENRAGED.toList(),
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.ENRAGED.toList()
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

    @Test
    fun `frustration should cool down when positive events happen`() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = 1
                probabilityOfFrustration = 100
            }
        )

        val negativeEmotions =
            OTHER_NEGATIVE_EMOTIONS
        val frustrated = Mood.FRUSTRATED.toList()
        listOf(
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.POSITIVE
            ) to Mood.RELIEVED.toList(),
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.POSITIVE
            ) to listOf(Mood.HAPPY, Mood.EXCITED),
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to negativeEmotions,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.POSITIVE
            ) to Mood.RELIEVED.toList(),
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to frustrated,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to listOf(Mood.FRUSTRATED, Mood.ENRAGED)
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

private val projectMock = mockk<Project>()

internal fun buildMotivationEvent(
    type: MotivationEvents,
    category: MotivationEventCategory
): MotivationEvent {
    return MotivationEvent(
        type,
        category,
        "我会写汉字",
        projectMock
    ) {
        AlertConfiguration(
            isAlertEnabled = true,
            isDisplayNotificationEnabled = true,
            isSoundAlertEnabled = true
        )
    }
}
