package zd.zero.waifu.motivator.plugin.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ServiceManager

object ApplicationService: Disposable {
    val instance: ApplicationService
        get() = ServiceManager.getService(ApplicationService::class.java)

    override fun dispose() {}
}
