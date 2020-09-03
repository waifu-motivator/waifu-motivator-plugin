package zd.zero.waifu.motivator.plugin.personality

import com.intellij.openapi.project.Project
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Test
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState

class EmotionCoreTest {

    private val projectMock = mockk<Project>()

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
        ).forEach {
            Assertions.assertThat(
                emotionCore.deriveMood(
                    it
                )
            ).isEqualTo(Mood.CALM)
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

        listOf(
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to Mood.CALM,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.CALM,
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to Mood.CALM,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.CALM,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.FRUSTRATED,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.FRUSTRATED
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
            ).isEqualTo(arguments.second)
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

        listOf(
            buildMotivationEvent(
                MotivationEvents.TASK,
                MotivationEventCategory.NEGATIVE
            ) to Mood.CALM,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.FRUSTRATED,
            buildMotivationEvent(
                MotivationEvents.IDLE,
                MotivationEventCategory.NEUTRAL
            ) to Mood.CALM,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.CALM,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.FRUSTRATED,
            buildMotivationEvent(
                MotivationEvents.TEST,
                MotivationEventCategory.NEGATIVE
            ) to Mood.FRUSTRATED
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
            ).isEqualTo(arguments.second)
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

        val expectedMood = Mood.CALM
        repeat(42) { index ->
            val deriveMood = emotionCore.deriveMood(
                motivationEvent
            )
            Assertions.assertThat(
                deriveMood
            ).withFailMessage(
                """At index #$index
                    |$motivationEvent
                    |did not create $expectedMood but did $deriveMood
                """.trimMargin()
            ).isEqualTo(expectedMood)
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
            EmotionCore.OTHER_NEGATIVE_EMOTIONS.toTypedArray()
        repeat(42) { index ->
            val deriveMood = emotionCore.deriveMood(
                motivationEvent
            )
            Assertions.assertThat(
                deriveMood
            ).withFailMessage(
                """At index #$index
                    |$motivationEvent
                    |did not create ${EmotionCore.OTHER_NEGATIVE_EMOTIONS} but did $deriveMood
                """.trimMargin()
            ).isIn(
                *negativeEmotions
            )
        }
    }

    private fun buildMotivationEvent(
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
}
