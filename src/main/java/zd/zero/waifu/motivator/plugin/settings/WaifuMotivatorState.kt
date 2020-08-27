package zd.zero.waifu.motivator.plugin.settings

class WaifuMotivatorState {
    companion object {
        const val DEFAULT_IDLE_TIMEOUT_IN_MINUTES: Long = 5L
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

    var idleTimeoutInMinutes = DEFAULT_IDLE_TIMEOUT_IN_MINUTES

    var version = "v0.0.0"

    var userId = ""
}
