package zd.zero.waifu.motivator.plugin.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager

object ApplicationService : Disposable {
    val instance: ApplicationService
        get() = ApplicationManager.getApplication().getService(ApplicationService::class.java)

    override fun dispose() {}
}
