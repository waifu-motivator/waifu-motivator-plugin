import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors.joining

open class HtmlToMdExtension : DefaultTask() {

    init {
        group = "documentation"
        description = "Reverts back the md extension"
    }

    @TaskAction
    fun run() {
        val releaseNotes = Paths.get(
            getBuildDirectory(project).toString(),
            "html",
            "RELEASE-NOTES.html"
        )

        Files.newBufferedWriter(releaseNotes).use {
            it.write(
                Files.newBufferedReader(releaseNotes).lines()
                    .map { l -> l.replace(".html", ".md") }
                    .collect(joining("\n"))
            )
        }
    }
}
