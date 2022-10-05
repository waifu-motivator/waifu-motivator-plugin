package zd.zero.waifu.motivator.plugin.promotion

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.DoNotAskOption
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.installAndEnable
import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import icons.WaifuMotivatorIcons
import org.intellij.lang.annotations.Language
import zd.zero.waifu.motivator.plugin.MessageBundle
import zd.zero.waifu.motivator.plugin.assets.AssetCategory
import zd.zero.waifu.motivator.plugin.assets.AssetManager
import zd.zero.waifu.motivator.plugin.assets.AssetManager.ASSETS_SOURCE
import zd.zero.waifu.motivator.plugin.assets.AssetManager.FALLBACK_ASSET_SOURCE
import zd.zero.waifu.motivator.plugin.service.AMII_PLUGIN_ID
import zd.zero.waifu.motivator.plugin.tools.toHexString
import java.awt.Dimension
import java.awt.Window
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent

class PromotionAssets(
    val isNewUser: Boolean
) {

    val pluginLogoURL: String
    val promotionAssetURL: String

    init {
        pluginLogoURL = getPluginLogo()
        promotionAssetURL = getPromotionAsset()
    }

    private fun getPluginLogo(): String = AssetManager.resolveAssetUrl(
        AssetCategory.PROMOTION,
        "amii/logo.png"
    ).orElse("$ASSETS_SOURCE/promotion/amii/logo.png")

    private fun getPromotionAsset(): String =
        AssetManager.resolveAssetUrl(AssetCategory.PROMOTION, "motivator/promotion.gif")
            .orElse("$FALLBACK_ASSET_SOURCE/promotion/motivator/promotion.gif")
}

class AniMemePromotionDialog(
    private val promotionAssets: PromotionAssets,
    parent: Window,
    private val onPromotion: (PromotionResults) -> Unit,
    private val project: Project
) : DialogWrapper(parent, true) {

    companion object {
        private const val INSTALLED_EXIT_CODE = 69
    }

    init {
        title = MessageBundle.message("amii.name")
        setCancelButtonText(MessageBundle.message("promotion.action.cancel"))
        setDoNotAskOption(
            DoNotPromote { shouldContinuePromotion, exitCode ->
                onPromotion(
                    PromotionResults(
                        when {
                            !shouldContinuePromotion -> PromotionStatus.BLOCKED
                            exitCode == INSTALLED_EXIT_CODE -> PromotionStatus.ACCEPTED
                            else -> PromotionStatus.REJECTED
                        }
                    )
                )
            }
        )
        init()
    }

    override fun createActions(): Array<Action> {
        return arrayOf(
            buildInstallAction(),
            cancelAction
        )
    }

    private fun buildInstallAction(): AbstractAction {
        return object : AbstractAction() {
            init {
                val message = MessageBundle.message("promotion.action.ok")
                putValue(NAME, message)
                putValue(SMALL_ICON, WaifuMotivatorIcons.Plugins.AMII.TOOL_WINDOW)
            }

            override fun actionPerformed(e: ActionEvent) {
                installAndEnable(
                    project,
                    setOf(PluginId.getId(AMII_PLUGIN_ID))
                ) {
                    close(INSTALLED_EXIT_CODE, true)
                }
            }
        }
    }

    override fun createCenterPanel(): JComponent =
        buildPromotionPane()

    @Suppress("LongMethod")
    private fun buildPromotionPane(): JEditorPane {
        val pane = JTextPane()
        pane.isEditable = false
        pane.contentType = "text/html"
        val accentHex = JBColor.namedColor(
            "Link.activeForeground",
            UIUtil.getTextAreaForeground()
        ).toHexString()
        val infoForegroundHex = UIUtil.getContextHelpForeground().toHexString()
        val pluginLogoURL = promotionAssets.pluginLogoURL
        pane.background = JBColor.namedColor(
            "Menu.background",
            UIUtil.getEditorPaneBackground()
        )

        pane.text =
            """
      <html lang="en">
      <head>
          <style type='text/css'>
              body {
                font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
              }
              .center {
                text-align: center;
              }
              a {
                  color: $accentHex;
                  font-weight: bold;
              }
              ul {
                  list-style-type: none;
              }
              p, div, ul, li {
                color: ${UIUtil.getLabelForeground().toHexString()};
              }
              h2 {
                margin: 16px 0;
                font-weight: bold;
                font-size: 22px;
              }
              h3 {
                margin: 4px 0;
                font-weight: bold;
                font-size: 14px;
              }
              .accented {
                color: $accentHex;
              }
              .info-foreground {
                color: $infoForegroundHex;
                text-align: center;
              }
              .header {
                color: $accentHex;
                text-align: center;
              }
              .logo-container {
                margin-top: 8px;
                text-align: center;
              }
              .display-image {
                max-height: 256px;
                text-align: center;
              }
          </style>
          <title>Motivator</title>
      </head>
      <body>
      <div class='logo-container'><img src="$pluginLogoURL" class='display-image' alt='Ani-Meme Plugin Logo'/>
      </div>
      ${getPromotionContent(promotionAssets.isNewUser)}
      <br/>
      </body>
      </html>
            """.trimIndent()
        pane.preferredSize = Dimension(pane.preferredSize.width + 120, pane.preferredSize.height)
        pane.addHyperlinkListener {
            if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                BrowserUtil.browse(it.url)
            }
        }

        return pane
    }

    private fun getPromotionContent(newUser: Boolean): String {
        return if (newUser) newUserPromotion()
        else existingUserPromotion()
    }

    @Language("HTML")
    private fun newUserPromotion(): String {
        val promotionAssetURL = promotionAssets.promotionAssetURL

        return """
        <h2 class='header'>Your new virtual companion!</h2>
        <div style='text-align: center;'>
            <p>
                <a href='https://plugins.jetbrains.com/plugin/15865-amii'>The Anime Meme plugin</a>
                gives your IDE more personality by using anime memes.<br/>
                You will get an assistant that will interact with you as you build code.<br/>
                Such as when your programs fail to run or tests pass/fail. Your companion<br/>
                has the ability to react to these events. Which will most likely take the form <br/> of a reaction gif of
                your favorite character(s)!
            </p>
        </div>
        <br/>
        <h3 class='info-foreground'>Bring Anime Memes to your IDE today!</h3>
        <div class='display-image'><img src='$promotionAssetURL' height="150" alt="Promotion Asset Image"/></div>
        <br/>
        """.trimIndent()
    }

    @Language("HTML")
    private fun existingUserPromotion(): String {
        return """
        <h2 class='header'>A brand-new experience!</h2>
        <div style='text-align: center;'>
            <p>
                As of Waifu Motivator v2.0, notifications have been moved to
                <a href='https://plugins.jetbrains.com/plugin/15865-amii'>the Anime Meme</a> plugin. <br><br>
                <div>
                    <b>Whats better?</b><br/>
                    <ul>
                        <li>✅ More Content!</li>
                        <li>✅ More Customization!</li>
                    </ul>
                    <br/>
                    <b>Breaking Changes:</b>
                    <ul>
                        <li>💥 Removed titled notifications.</li>
                        <li>💥 Your previous configurations will be lost (Sorry!).</li>
                    </ul>
                </div>

                For a list of more breaking changes and enhancements please see
                <a href="https://github.com/waifu-motivator/waifu-motivator-plugin/blob/main/CHANGELOG.md">the changelog</a>
            </p>
        </div>
        <br/>
        <h3 class='info-foreground'>I hope you enjoy!</h3>
        <br/>
        """.trimIndent()
    }
}

class DoNotPromote(
    private val onToBeShown: (Boolean, Int) -> Unit
) : DoNotAskOption {
    override fun isToBeShown(): Boolean = true

    override fun setToBeShown(toBeShown: Boolean, exitCode: Int) {
        onToBeShown(toBeShown, exitCode)
    }

    override fun canBeHidden(): Boolean = true

    override fun shouldSaveOptionsOnCancel(): Boolean = true

    override fun getDoNotShowMessage(): String =
        MessageBundle.message("promotions.dont.ask")
}
