package zd.zero.waifu.motivator.plugin.promotion

import com.google.gson.GsonBuilder
import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.assets.AssetCategory
import zd.zero.waifu.motivator.plugin.assets.AssetManager
import zd.zero.waifu.motivator.plugin.assets.LocalStorageService
import zd.zero.waifu.motivator.plugin.tools.ExceptionTools.runSafely
import zd.zero.waifu.motivator.plugin.tools.runSafelyWithResult
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.Instant
import java.util.UUID
import kotlin.io.path.exists

data class Promotion(
    val id: UUID,
    val datePromoted: Instant,
    val result: PromotionStatus,
)

data class PromotionLedger(
    val user: UUID,
    val versionInstallDates: MutableMap<String, Instant>,
    val seenPromotions: MutableMap<UUID, Promotion>,
    var allowedToPromote: Boolean,
)

object LedgerMaster {
    private val log = Logger.getInstance(LedgerMaster::class.java)

    private val gson =
        GsonBuilder()
            .setPrettyPrinting()
            .create()

    private val ledgerPath =
        AssetManager.constructGlobalAssetPath(
            AssetCategory.PROMOTION,
            "ledger.json",
        ).orElseGet {
            AssetManager.constructLocalAssetPathNoOptional(
                AssetCategory.PROMOTION,
                "ledger.json",
            )
        }

    fun getInitialLedger(): PromotionLedger =
        if (ledgerPath.exists()) {
            readLedger()
        } else {
            PromotionLedger(UUID.randomUUID(), mutableMapOf(), mutableMapOf(), true)
        }

    fun readLedger(): PromotionLedger =
        runSafelyWithResult({
            Files.newInputStream(ledgerPath)
                .use {
                    gson.fromJson(
                        InputStreamReader(it, StandardCharsets.UTF_8),
                        PromotionLedger::class.java,
                    )
                }
        }) {
            log.warn("Unable to read promotion ledger for raisins.", it)
            PromotionLedger(UUID.randomUUID(), mutableMapOf(), mutableMapOf(), true)
        }

    fun persistLedger(promotionLedger: PromotionLedger) {
        if (ledgerPath.exists().not()) {
            LocalStorageService.createDirectories(ledgerPath)
        }

        runSafely({
            Files.newBufferedWriter(
                ledgerPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
            ).use {
                it.write(
                    gson.toJson(promotionLedger),
                )
            }
        }) {
            log.warn("Unable to persist ledger for raisins", it)
        }
    }
}
