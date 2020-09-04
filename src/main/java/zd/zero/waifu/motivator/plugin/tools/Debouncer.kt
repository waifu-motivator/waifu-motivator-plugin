package zd.zero.waifu.motivator.plugin.tools

import com.intellij.util.Alarm

interface Debouncer {
    companion object {
        const val DEFAULT_INTERVAL = 250
    }

    fun debounce(toDebounce: () -> Unit)
}

class AlarmDebouncer(private val interval: Int) : Debouncer {
    private val alarm: Alarm = Alarm()

    @Volatile
    private var lastInvoked: Long = 0

    override fun debounce(toDebounce: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (lastInvoked + interval < currentTime) {
            lastInvoked = currentTime
            alarm.cancelAllRequests()
            alarm.addRequest({
                toDebounce()
                alarm.cancelAllRequests()
            }, interval)
        }
    }
}
