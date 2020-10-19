package zd.zero.waifu.motivator.plugin.personality.core.emotions

import com.intellij.util.messages.Topic

interface MoodListener {
    fun onDerivedMood(currentMood: Mood)
}

val EMOTION_TOPIC = Topic(
    "Current Mood",
    MoodListener::class.java
)

enum class EmotionalMutationType {
    COOL_DOWN
}
enum class MoodCategory {
    POSITIVE, NEGATIVE, NEUTRAL
}
data class EmotionalMutationAction(
    val type: EmotionalMutationType,
    val moodCategory: MoodCategory
)
interface EmotionalMutationActionListener {
    fun onAction(emotionalMutationAction: EmotionalMutationAction)
}
val EMOTIONAL_MUTATION_TOPIC = Topic(
    "Mood Mutation",
    EmotionalMutationActionListener::class.java
)
