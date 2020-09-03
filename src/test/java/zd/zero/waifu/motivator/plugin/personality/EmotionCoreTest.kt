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
                eventsBeforeFrustration = 5
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
            Assertions.assertThat(
                emotionCore.deriveMood(
                    arguments.first
                )
            ).withFailMessage(
                """At index #$index
                    |${arguments.first}
                    |did not create ${arguments.second}
                """.trimMargin()
            ).isEqualTo(arguments.second)
        }
    }

    @Test
    fun shouldCalmDownAfterIdleEvent() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = 2
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
            Assertions.assertThat(
                emotionCore.deriveMood(
                    arguments.first
                )
            ).withFailMessage(
                """At index #$index
                    |${arguments.first}
                    |did not create ${arguments.second}
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
            Assertions.assertThat(
                emotionCore.deriveMood(
                    motivationEvent
                )
            ).withFailMessage(
                """At index #$index
                    |$motivationEvent
                    |did not create $expectedMood
                """.trimMargin()
            ).isEqualTo(expectedMood)
        }
    }

    @Test
    fun deriveMoodShouldReturnNeverReturnFrustratedWhenProbabilityIsZero() {
        val emotionCore = EmotionCore(
            WaifuMotivatorState().apply {
                eventsBeforeFrustration = 1
                probabilityOfFrustration = 0
            }
        )

        val motivationEvent = buildMotivationEvent(
            MotivationEvents.TASK,
            MotivationEventCategory.NEGATIVE
        )

        val expectedMood = Mood.CALM
        repeat(42) { index ->
            Assertions.assertThat(
                emotionCore.deriveMood(
                    motivationEvent
                )
            ).withFailMessage(
                """At index #$index
                    |$motivationEvent
                    |did not create $expectedMood
                """.trimMargin()
            ).isEqualTo(expectedMood)
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
