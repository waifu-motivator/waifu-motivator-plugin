package zd.zero.waifu.motivator.plugin

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.StartupManager
import zd.zero.waifu.motivator.plugin.onboarding.UserOnboarding

class WaifuMotivatorProject : ProjectManagerListener, Disposable {

    private var project: Project? = null

    override fun projectOpened(projectOpened: Project) {
        if (project != null) return

        project = projectOpened
        StartupManager.getInstance(project!!)
            .runWhenProjectIsInitialized { UserOnboarding.attemptToPerformNewUpdateActions() }
    }

    override fun projectClosing(project: Project) {
        if (project === this.project) dispose()
    }

    override fun dispose() {}

}
