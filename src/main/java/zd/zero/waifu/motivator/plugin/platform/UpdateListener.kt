package zd.zero.waifu.motivator.plugin.platform

import com.intellij.util.messages.Topic

interface UpdateListener {
    companion object {
        val TOPIC = Topic.create("Motivator Update", UpdateListener::class.java)
    }

    fun onUpdate()
}
