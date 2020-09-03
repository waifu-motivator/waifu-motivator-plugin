package zd.zero.waifu.motivator.plugin.personality

import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent

interface PersonalityCore {
    fun processMotivationEvent(
        motivationEvent: MotivationEvent,
        mood: Mood
    )
}
