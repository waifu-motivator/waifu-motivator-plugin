package zd.zero.waifu.motivator.plugin.tools

import com.intellij.util.containers.concat
import zd.zero.waifu.motivator.plugin.personality.core.emotions.Mood
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.random.Random

class ProbabilityTools(
    private val random: Random
) {
    companion object {
        private const val TOTAL_WEIGHT = 100
    }

    fun pickEmotion(
        probabilityOfPrimaryEmotions: Int,
        primaryEmotions: Stream<Pair<Mood, Int>>,
        secondaryEmotions: List<Mood>
    ): Mood {
        assert(probabilityOfPrimaryEmotions in 0..TOTAL_WEIGHT) { "Expected probability to be from 0 to 100" }
        val weightRemaining = TOTAL_WEIGHT - probabilityOfPrimaryEmotions
        val weightedEmotions = buildWeightedList(
            weightRemaining,
            primaryEmotions,
            secondaryEmotions
        )
        return pickFromWeightedList(
            random.nextInt(1, TOTAL_WEIGHT),
            weightedEmotions
        )
    }

    private fun buildWeightedList(
        weightRemaining: Int,
        primaryEmotions: Stream<Pair<Mood, Int>>,
        secondaryEmotions: List<Mood>
    ): List<Pair<Mood, Int>> {
        val secondaryEmotionWeights = weightRemaining / secondaryEmotions.size
        return concat(
                primaryEmotions,
                secondaryEmotions.stream().map { it to secondaryEmotionWeights }
        ).collect(Collectors.toList())
            .shuffled<Pair<Mood, Int>>()
    }

    private fun pickFromWeightedList(
        weightChosen: Int,
        weightedEmotions: List<Pair<Mood, Int>>
    ): Mood {
        var randomWeight = weightChosen
        for ((mood, weight) in weightedEmotions) {
            if (randomWeight <= weight) {
                return mood
            }
            randomWeight -= weight
        }

        return weightedEmotions.first { it.second > 0 }.first
    }
}
