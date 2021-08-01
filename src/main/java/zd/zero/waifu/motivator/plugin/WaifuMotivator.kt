package zd.zero.waifu.motivator.plugin

class WaifuMotivator private constructor() {

    companion object {
        const val PLUGIN_ID = "zd.zero.waifu-motivator-plugin"
        const val PLUGIN_NAME = "Waifu Motivator"
    }

    init {
        throw AssertionError("Never instantiate.")
    }
}
