package zd.zero.waifu.motivator.plugin.motivation

val defaultListener = object : MotivationLifecycleListener {
    override fun onDisplay() {}

    override fun onDispose() {}
}

interface MotivationLifecycleListener {

    fun onDisplay()

    fun onDispose()
}
