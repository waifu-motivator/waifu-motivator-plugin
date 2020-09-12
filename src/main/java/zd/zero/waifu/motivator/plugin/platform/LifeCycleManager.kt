package zd.zero.waifu.motivator.plugin.platform

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.MessageBusConnection

object LifeCycleManager : Disposable {

    private lateinit var connection: MessageBusConnection

    fun init() {
        if (this::connection.isInitialized.not()) {
            connection = ApplicationManager.getApplication().messageBus.connect()
        }
    }

    fun registerUpdateListener(updateAssetsListener: UpdateAssetsListener) {
        if (this::connection.isInitialized) {
            connection.subscribe(UpdateAssetsListener.TOPIC, updateAssetsListener)
        }
    }

    override fun dispose() {
        if (this::connection.isInitialized) {
            connection.dispose()
        }
    }
}
