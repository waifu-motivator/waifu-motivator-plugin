package zd.zero.waifu.motivator.plugin.integrations

import com.google.gson.GsonBuilder
import com.intellij.ide.IdeBundle
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.util.registry.Registry
import com.intellij.util.Consumer
import com.intellij.util.text.DateFormatUtil
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.protocol.Message
import io.sentry.protocol.User
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.RestClient
import java.awt.Component
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Properties
import java.util.stream.Collectors

class ErrorReporter : ErrorReportSubmitter() {
    override fun getReportActionText(): String = "Report Anonymously"

    companion object {
        private val gson = GsonBuilder().create()

        init {
            Sentry.init { options: SentryOptions ->
                options.dsn = RestClient.performGet(
                    "https://jetbrains.assets.unthrottled.io/waifu-motivator/sentry-dsn.txt"
                )
                    .map { it.trim() }
                    .orElse("https://3630573c245444f8b49ef498b24d1405@o403546.ingest.sentry.io/5374288?maxmessagelength=50000")
            }
            Sentry.setUser(
                User().apply {
                    this.id = WaifuMotivatorPluginState.getPluginState().userId
                }
            )
        }
    }

    override fun submit(
        events: Array<out IdeaLoggingEvent>,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<in SubmittedReportInfo>
    ): Boolean {
        ApplicationManager.getApplication()
            .executeOnPooledThread {
                events.forEach {
                    Sentry.captureEvent(
                        addSystemInfo(
                            SentryEvent()
                                .apply {
                                    this.level = SentryLevel.ERROR
                                    this.serverName = getAppName().second
                                    this.setExtra("Additional Info", additionalInfo ?: "None")
                                }
                        ).apply {
                            this.message = Message().apply {
                                this.message = it.throwableText
                            }
                        }
                    )
                }
                consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE))
            }
        return true
    }

    private fun addSystemInfo(event: SentryEvent): SentryEvent {
        val pair = getAppName()
        val appInfo = pair.first
        val appName = pair.second
        val properties = System.getProperties()
        return event.apply {
            setExtra("App Name", appName)
            setExtra("Version", WaifuMotivatorPluginState.getPluginState().version)
            setExtra("Build Info", getBuildInfo(appInfo))
            setExtra("JRE", getJRE(properties))
            setExtra("VM", getVM(properties))
            setExtra("System Info", SystemInfo.getOsNameAndVersion())
            setExtra("GC", getGC())
            setExtra("Memory", Runtime.getRuntime().maxMemory() / FileUtilRt.MEGABYTE)
            setExtra("Cores", Runtime.getRuntime().availableProcessors())
            setExtra("Registry", getRegistry())
            setExtra("Non-Bundled Plugins", getNonBundledPlugins())
            setExtra("Plugin Config", getMinifiedConfig())
        }
    }

    private fun getJRE(properties: Properties): String {
        val javaVersion =
            properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown"))
        val arch = properties.getProperty("os.arch", "")
        return IdeBundle.message("about.box.jre", javaVersion, arch)
    }

    private fun getVM(properties: Properties): String {
        val vmVersion = properties.getProperty("java.vm.name", "unknown")
        val vmVendor = properties.getProperty("java.vendor", "unknown")
        return IdeBundle.message("about.box.vm", vmVersion, vmVendor)
    }

    private fun getNonBundledPlugins(): String {
        return Arrays.stream(PluginManagerCore.getPlugins())
            .filter { it.isBundled.not() && it.isEnabled }
            .map { it.pluginId.idString }.collect(Collectors.joining(","))
    }

    private fun getRegistry() = Registry.getAll().stream().filter { it.isChangedFromDefault }
        .map { "${it.key}=${it.asString()}" }.collect(Collectors.joining(","))

    private fun getGC() = ManagementFactory.getGarbageCollectorMXBeans().stream()
        .map { it.name }.collect(Collectors.joining(","))

    private fun getBuildInfo(appInfo: ApplicationInfo): String {
        var buildInfo = IdeBundle.message("about.box.build.number", appInfo.build.asString())
        val cal = appInfo.buildDate
        var buildDate = ""
        if (appInfo.build.isSnapshot) {
            buildDate = SimpleDateFormat("HH:mm, ").format(cal.time)
        }
        buildDate += DateFormatUtil.formatAboutDialogDate(cal.time)
        buildInfo += IdeBundle.message("about.box.build.date", buildDate)
        return buildInfo
    }

    private fun getAppName(): Pair<ApplicationInfo, String> {
        val appInfo = ApplicationInfo.getInstance()
        var appName = appInfo.fullApplicationName
        val edition = ApplicationNamesInfo.getInstance().editionName
        if (edition != null) appName += " ($edition)"
        return Pair(appInfo, appName)
    }

    private fun getMinifiedConfig(): String {
        val keyMapper = fun(s: String): String {
            val capitals = s.codePoints()
                .filter { Character.isUpperCase(it) }
                .mapToObj { Character.toChars(it).joinToString() }
                .collect(Collectors.joining())

            return if (capitals.isEmpty()) return s[0].toString()
            else capitals.toLowerCase()
        }
        val valueMapper = fun(s: String): String = when {
            "true".equals(s, ignoreCase = true) -> "1"
            "false".equals(s, ignoreCase = true) -> "0"
            else -> s
        }

        val pluginState = WaifuMotivatorPluginState.getPluginState()

        val minifiedConfig = StringBuilder()
        val excludedProperties = listOf("userId", "version", "preferredCharacters")
        val root = gson.toJsonTree(pluginState).asJsonObject
        val iterator = root.entrySet().iterator().withIndex()
        while (iterator.hasNext()) {
            val (index, item) = iterator.next()
            val (key, element) = item

            if (excludedProperties.contains(key).not()) {
                if (index != 0) minifiedConfig.append(";")
                minifiedConfig.append(keyMapper(key))
                    .append(":")

                when {
                    element.isJsonPrimitive && element.asString.isNotEmpty() ->
                        minifiedConfig.append(valueMapper(element.asString))
                    else ->
                        minifiedConfig.append(element.toString())
                }
            }
        }

        return minifiedConfig.toString()
    }
}
