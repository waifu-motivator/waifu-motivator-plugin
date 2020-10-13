package zd.zero.waifu.motivator.plugin.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import java.util.*

@State(
    name = "WaifuMotivatorPluginStatistics",
    storages = [
        Storage(
            value = "waifu-motivator-plugin-statistics.xml",
            roamingType = RoamingType.DISABLED
        )
    ]
)
class MotivationStatistics : PersistentStateComponent<MotivationStatistics> {

    companion object {
        const val DEFAULT_STATISTICS_VALUE = 0

        private val eventTypesToRegister = setOf(MotivationEvents.TASK, MotivationEvents.TEST)
    }

    private val statistics: MutableMap<MotivationEvents, Int> =
        Collections.synchronizedMap(EnumMap(MotivationEvents::class.java))

    fun registerEvent(event: MotivationEvent) {
        if (eventTypesToRegister.contains(event.type).not()) return

        synchronized(statistics) {
            var eventCount = statistics.getOrDefault(event.type, DEFAULT_STATISTICS_VALUE)
            eventCount++
            statistics.put(event.type, eventCount)
        }
    }

    fun getEventStat(event: MotivationEvents): Int = statistics.getOrDefault(event, DEFAULT_STATISTICS_VALUE)

    fun resetStatistics() {
        statistics.clear()
    }

    override fun getState(): MotivationStatistics? = this

    override fun loadState(state: MotivationStatistics) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
