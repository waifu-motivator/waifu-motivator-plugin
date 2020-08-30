package zd.zero.waifu.motivator.plugin

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.listeners.IdleEventListener

object WaifuProjectManager : Disposable {

    private lateinit var project: Project
    private lateinit var idleEventListener: IdleEventListener

    fun projectOpened(openedProject: Project) {
        if (this::project.isInitialized.not()) {
            this.project = openedProject
            this.idleEventListener = IdleEventListener()
        }
    }

    override fun dispose() {
        if (this::project.isInitialized) {
            this.idleEventListener.dispose()
        }
    }
}
