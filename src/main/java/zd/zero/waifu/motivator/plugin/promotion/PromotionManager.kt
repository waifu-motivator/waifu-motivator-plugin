package zd.zero.waifu.motivator.plugin.promotion

import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.promotion.AniMemePromotionService.runPromotion
import zd.zero.waifu.motivator.plugin.promotion.LedgerMaster.getInitialLedger
import zd.zero.waifu.motivator.plugin.promotion.LedgerMaster.persistLedger
import zd.zero.waifu.motivator.plugin.promotion.LockMaster.acquireLock
import zd.zero.waifu.motivator.plugin.promotion.LockMaster.releaseLock
import zd.zero.waifu.motivator.plugin.service.AppService.getApplicationName
import zd.zero.waifu.motivator.plugin.service.PluginService.isAmiiInstalled
import java.time.Instant
import java.util.UUID

val ANI_MEME_PROMOTION_ID: UUID = UUID.fromString("af735f6e-2a63-4c4e-b26d-6fe215892a43")

object PromotionManager : PromotionManagerImpl()

open class PromotionManagerImpl {
    private val log = Logger.getInstance(PromotionManager::class.java)

    private var initialized = false

    private val promotionLedger: PromotionLedger = getInitialLedger()

    fun registerPromotion(
        newVersion: String,
        forceRegister: Boolean = false,
        isNewUser: Boolean = false,
    ) {
        if (initialized.not() || forceRegister) {
            promotionRegistry(newVersion, isNewUser)
            initialized = true
        }
    }

    private fun promotionRegistry(
        newVersion: String,
        isNewUser: Boolean,
    ) {
        val versionInstallDates = promotionLedger.versionInstallDates
        if (versionInstallDates.containsKey(newVersion).not()) {
            versionInstallDates[newVersion] = Instant.now()
            persistLedger(promotionLedger)
        }
        setupPromotion(isNewUser)
    }

    private fun setupPromotion(isNewUser: Boolean) {
        if (isAniMemePluginInstalled().not() && shouldPromote()) {
            try {
                if (acquireLock(id)) {
                    runPromotion(
                        isNewUser,
                        {
                            promotionLedger.allowedToPromote = it.status != PromotionStatus.BLOCKED
                            promotionLedger.seenPromotions[ANI_MEME_PROMOTION_ID] =
                                Promotion(ANI_MEME_PROMOTION_ID, Instant.now(), it.status)
                            persistLedger(promotionLedger)
                            releaseLock(id)
                        },
                    ) {
                        releaseLock(id)
                    }
                }
            } catch (e: Throwable) {
                log.warn("Unable to promote for raisins.", e)
            }
        }
    }

    private fun isAniMemePluginInstalled() = isAmiiInstalled()

    private val id: String
        get() = getApplicationName()

    private fun shouldPromote(): Boolean =
        promotionLedger.allowedToPromote &&
            (
                promotionLedger.seenPromotions.containsKey(ANI_MEME_PROMOTION_ID).not() ||
                    promotionLedger.seenPromotions[ANI_MEME_PROMOTION_ID]?.result == PromotionStatus.ACCEPTED
            )
}
