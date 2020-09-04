package zd.zero.waifu.motivator.plugin.personality.core

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.personality.core.emotions.Mood

interface PersonalityCore {
    fun processMotivationEvent(
        motivationEvent: MotivationEvent,
        mood: Mood
    )
}
