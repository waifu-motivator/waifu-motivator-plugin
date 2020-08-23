package zd.zero.waifu.motivator.plugin.settings

import java.util.concurrent.TimeUnit

class WaifuMotivatorState {
    companion object {
        val DEFAULT_IDLE_TIMEOUT = TimeUnit.MILLISECONDS.convert(5L, TimeUnit.MINUTES)
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

    var isIdleMotivationEnabled = true

    var isIdleSoundEnabled = true

    var idleTimout = DEFAULT_IDLE_TIMEOUT

    var version = "v0.0.0"

    var userId = ""
}
