package zd.zero.waifu.motivator.plugin.motivation.event

import com.intellij.util.messages.Topic

interface MotivationEventListener {

    companion object {
        val TOPIC: Topic<MotivationEventListener> = Topic.create(
                "Motivation Events",
                MotivationEventListener::class.java
        )
    }

    fun onEventTrigger(motivationEvent: MotivationEvent)
}
