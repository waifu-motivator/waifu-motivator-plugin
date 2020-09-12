package zd.zero.waifu.motivator.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.platform.UpdateAssetsListener

class AssetSyncAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(UpdateAssetsListener.TOPIC)
            .onRequestedUpdate()
        UpdateNotification.sendMessage(
            "Assets Synchronized",
            "Your local lists of assets should now be up to date with the remote repository.",
            e.project
        )
    }
}
