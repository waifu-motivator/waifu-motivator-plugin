package zd.zero.waifu.motivator.plugin.platform

import com.intellij.util.messages.Topic

interface UpdateAssetsListener {
    companion object {
        val TOPIC = Topic.create("Motivator Assets Update", UpdateAssetsListener::class.java)
    }

    fun requestUpdate()
}
