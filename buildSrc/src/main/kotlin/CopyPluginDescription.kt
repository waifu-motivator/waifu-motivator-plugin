import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

const val PLUGIN_DESCRIPTION_FILE = "plugin.md"

open class CopyPluginDescription : DefaultTask() {

  init {
    group = "documentation"
    description = "Copies the plugin description to a place where it can be converted with other markdown files"
  }

  @TaskAction
  fun run() {
    val markdownPath = createMarkdownDirectory(project)
    val readmeMarkdown = Paths.get(
      markdownPath.toString(),
      PLUGIN_DESCRIPTION_FILE
    )
    Files.copy(
      Paths.get(project.rootDir.absolutePath, "docs", PLUGIN_DESCRIPTION_FILE),
      readmeMarkdown,
      StandardCopyOption.REPLACE_EXISTING
    )
  }
}
