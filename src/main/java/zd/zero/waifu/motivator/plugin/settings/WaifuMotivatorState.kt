package zd.zero.waifu.motivator.plugin.settings

import zd.zero.waifu.motivator.plugin.listeners.FORCE_KILLED_EXIT_CODE
import zd.zero.waifu.motivator.plugin.listeners.OK_EXIT_CODE

class WaifuMotivatorState {
    companion object {
        const val DEFAULT_DELIMITER = ","
        const val DEFAULT_IDLE_TIMEOUT_IN_MINUTES: Long = 5L
        const val DEFAULT_EVENTS_BEFORE_FRUSTRATION: Int = 5
        const val DEFAULT_FRUSTRATION_PROBABILITY: Int = 75
    }

    var isWaifuOfTheDayEnabled = true

    var isStartupMotivationEnabled = true

    var isStartupMotivationSoundEnabled = true

    var isUnitTesterMotivationEnabled = true

    var isUnitTesterMotivationSoundEnabled = true

    var isMotivateMeEnabled = true

    var isMotivateMeSoundEnabled = true

    var isSayonaraEnabled = true

    var isDisabledInDistractionFreeMode = true

    var isTaskMotivationEnabled = true

    var isTaskSoundEnabled = true

    var isIdleMotivationEnabled = true

    var isIdleSoundEnabled = false

    var isExitCodeNotificationEnabled = true

    var isExitCodeSoundEnabled = true

    var idleTimeoutInMinutes = DEFAULT_IDLE_TIMEOUT_IN_MINUTES

    var isAllowFrustration = true

    var eventsBeforeFrustration = DEFAULT_EVENTS_BEFORE_FRUSTRATION

    var probabilityOfFrustration = DEFAULT_FRUSTRATION_PROBABILITY

    var allowedExitCodes = listOf(
        OK_EXIT_CODE,
        FORCE_KILLED_EXIT_CODE
    ).joinToString(DEFAULT_DELIMITER)

    var version = "v0.0.0"

    var userId = ""

    var allowedCharacters = ""
}
