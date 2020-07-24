package zd.zero.waifu.motivator.plugin.listeners

import com.intellij.util.Alarm

interface Debouncer {
    fun debounce(toDebounce: Runnable)
}

class JetbrainsDebouncer(private val debounceInterval: Int) : Debouncer {
    private val alarm: Alarm = Alarm()
    @Volatile
    private var lastInvoked: Long = 0

    override fun debounce(toDebounce: Runnable) {
        val currentTime = System.currentTimeMillis()
        if (lastInvoked + debounceInterval < currentTime) {
            lastInvoked = currentTime
            alarm.cancelAllRequests()
            alarm.addRequest(toDebounce, debounceInterval)
        }
    }
}
