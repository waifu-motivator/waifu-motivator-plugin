package zd.zero.waifu.motivator.plugin.tools

import com.intellij.openapi.Disposable
import com.intellij.util.Alarm
import java.util.LinkedList

fun interface Debouncer {
    fun debounce(toDebounce: () -> Unit)
}

fun interface BufferedDebouncer<T> {
    fun debounceAndBuffer(t: T, onDebounced: (List<T>) -> Unit)
}

class AlarmDebouncer<T>(private val interval: Int) :
    Debouncer,
    BufferedDebouncer<T>,
    Disposable {
    private val alarm: Alarm = Alarm()

    @Volatile
    private var lastInvoked: Long = 0

    override fun debounce(toDebounce: () -> Unit) {
        performDebounce({
            alarm.addRequest(toDebounce, interval)
        })
    }

    private val buffer = LinkedList<T>()

    override fun debounceAndBuffer(t: T, onDebounced: (List<T>) -> Unit) {
        performDebounce({
            buffer.add(t)
            alarm.addRequest(
                {
                    onDebounced(buffer.toMutableList())
                    buffer.clear()
                },
                interval
            )
        }) {
            buffer.push(t)
        }
    }

    private fun performDebounce(setupDebounce: () -> Unit, onDebounced: () -> Unit = {}) {
        val currentTime = System.currentTimeMillis()
        if (lastInvoked + interval < currentTime) {
            lastInvoked = currentTime
            alarm.cancelAllRequests()
            setupDebounce()
        } else {
            onDebounced()
        }
    }

    override fun dispose() {
        alarm.dispose()
    }
}
