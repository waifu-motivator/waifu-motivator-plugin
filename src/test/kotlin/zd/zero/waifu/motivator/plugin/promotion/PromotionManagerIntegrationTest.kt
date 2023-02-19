package zd.zero.waifu.motivator.plugin.promotion

import com.intellij.util.io.isFile
import io.mockk.Called
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import zd.zero.waifu.motivator.plugin.assets.LocalStorageService
import zd.zero.waifu.motivator.plugin.service.AppService
import zd.zero.waifu.motivator.plugin.service.PluginService
import zd.zero.waifu.motivator.plugin.test.tools.TestTools
import zd.zero.waifu.motivator.plugin.test.tools.TestTools.setUpMocksForManager
import zd.zero.waifu.motivator.plugin.test.tools.TestTools.tearDownMocksForPromotionManager
import zd.zero.waifu.motivator.plugin.tools.RestClient
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.nio.file.Files
import java.time.Instant
import java.time.Period
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

class PromotionManagerIntegrationTest {

    companion object {
        private const val testDirectory = "testOne"

        @JvmStatic
        @BeforeClass
        fun setUp() {
            setUpMocksForManager()
            mockkObject(AniMemePromotionService)
            mockkObject(RestClient)
            mockkObject(PluginService)
            mockkObject(AppService)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            tearDownMocksForPromotionManager()
            unmockkObject(AniMemePromotionService)
            unmockkObject(RestClient)
            unmockkObject(PluginService)
            unmockkObject(AppService)
        }
    }

    @Before
    fun cleanUp() {
        clearMocks(AniMemePromotionService)
        every { AppService.getApplicationName() } returns "零二"
        Files.walk(TestTools.getTestAssetPath(testDirectory))
            .filter { it.isFile() }
            .forEach { Files.deleteIfExists(it) }
    }

    @Test
    fun `should write new version`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val beforePromotion = Instant.now()

        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger.allowedToPromote).isTrue
        assertThat(postLedger.user).isNotNull
        assertThat(postLedger.seenPromotions.isEmpty()).isTrue
        assertThat(postLedger.versionInstallDates.size).isEqualTo(1)
        assertThat(postLedger.versionInstallDates["Ryuko"]).isBetween(
            beforePromotion,
            Instant.now()
        )

        validateLedgerCallback(postLedger, beforePromotion)

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should always write new version`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val beforeRyuko = Instant.now()

        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postRyukoLedger = LedgerMaster.readLedger()

        assertThat(postRyukoLedger.allowedToPromote).isTrue
        assertThat(postRyukoLedger.user).isNotNull
        assertThat(postRyukoLedger.seenPromotions.isEmpty()).isTrue
        assertThat(postRyukoLedger.versionInstallDates.size).isEqualTo(1)
        assertThat(postRyukoLedger.versionInstallDates["Ryuko"]).isBetween(
            beforeRyuko,
            Instant.now()
        )

        val beforeRin = Instant.now()

        promotionManager.registerPromotion("Rin", true)

        val postRinLedger = LedgerMaster.readLedger()

        assertThat(postRinLedger.allowedToPromote).isTrue
        assertThat(postRinLedger.user).isNotNull
        assertThat(postRinLedger.seenPromotions.isEmpty()).isTrue
        assertThat(postRinLedger.versionInstallDates.size).isEqualTo(2)
        assertThat(postRyukoLedger.versionInstallDates["Ryuko"]).isBetween(
            beforeRyuko,
            Instant.now()
        )
        assertThat(postRinLedger.versionInstallDates["Rin"]).isBetween(
            beforeRin,
            Instant.now()
        )

        validateLedgerCallback(postRinLedger, beforeRin)

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should not do anything when AMII is installed`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()

        every { PluginService.isAmiiInstalled() } returns true

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        verify { AniMemePromotionService wasNot Called }
    }

    @Test
    fun `should not do anything when has been promoted before`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(
                ANI_MEME_PROMOTION_ID to Promotion(
                    ANI_MEME_PROMOTION_ID,
                    Instant.now(),
                    PromotionStatus.REJECTED
                )
            ),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        verify { AniMemePromotionService wasNot Called }
    }

    @Test
    fun `should not do anything when not owner of lock`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        assertThat(LockMaster.acquireLock("Misato")).isTrue

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        verify { AniMemePromotionService wasNot Called }
    }

    @Test
    fun `should not promote when not allowed`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(),
            false
        )

        LedgerMaster.persistLedger(currentLedger)

        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        verify { AniMemePromotionService wasNot Called }

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should not promote when previous promotion was rejected`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(
                ANI_MEME_PROMOTION_ID to Promotion(ANI_MEME_PROMOTION_ID, Instant.now(), PromotionStatus.REJECTED)
            ),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        verify { AniMemePromotionService wasNot Called }

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should promote when previous promotion was accepted`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(
                ANI_MEME_PROMOTION_ID to Promotion(ANI_MEME_PROMOTION_ID, Instant.now(), PromotionStatus.ACCEPTED)
            ),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val beforePromotion = Instant.now()
        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        validateLedgerCallback(currentLedger, beforePromotion)

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should promote when previous promotion was not shown`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(
                ANI_MEME_PROMOTION_ID to Promotion(ANI_MEME_PROMOTION_ID, Instant.now(), PromotionStatus.ACCEPTED)
            ),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val beforePromotion = Instant.now()
        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        val promotionSlot = slot<(PromotionResults) -> Unit>()
        val rejectionSlot = slot<() -> Unit>()
        val newUserSlot = slot<Boolean>()
        verify {
            AniMemePromotionService.runPromotion(
                capture(newUserSlot),
                capture(promotionSlot),
                capture(rejectionSlot)
            )
        }

        rejectionSlot.captured()

        assertTrue { LockMaster.acquireLock("Syrena") }
        LockMaster.releaseLock("Syrena")

        validateLedgerCallback(currentLedger, beforePromotion)

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should promote when not locked`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val beforePromotion = Instant.now()
        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        validateLedgerCallback(currentLedger, beforePromotion)

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should promote when primary assets are down`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val beforePromotion = Instant.now()
        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        validateLedgerCallback(currentLedger, beforePromotion)

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    @Test
    fun `should break old lock`() {
        every { LocalStorageService.getGlobalAssetDirectory() } returns
            TestTools.getTestAssetPath(testDirectory).toString().toOptional()
        every { PluginService.isAmiiInstalled() } returns false

        LockMaster.writeLock(
            Lock(
                "Misato",
                Instant.now().minusMillis(
                    TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS)
                )
            )
        )

        val currentLedger = PromotionLedger(
            UUID.randomUUID(),
            mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
            mutableMapOf(),
            true
        )

        LedgerMaster.persistLedger(currentLedger)

        val beforePromotion = Instant.now()
        val promotionManager = PromotionManagerImpl()
        promotionManager.registerPromotion("Ryuko", true)

        val postLedger = LedgerMaster.readLedger()

        assertThat(postLedger).isEqualTo(currentLedger)

        validateLedgerCallback(currentLedger, beforePromotion)

        assertTrue { LockMaster.acquireLock("Syrena") }
    }

    private fun validateLedgerCallback(
        currentLedger: PromotionLedger,
        beforePromotion: Instant?
    ) {
        val promotionSlot = slot<(PromotionResults) -> Unit>()
        val rejectionSlot = slot<() -> Unit>()
        val newUserSlot = slot<Boolean>()
        verify {
            AniMemePromotionService.runPromotion(
                capture(newUserSlot),
                capture(promotionSlot),
                capture(rejectionSlot)
            )
        }

        val promotionCallback = promotionSlot.captured
        promotionCallback(PromotionResults(PromotionStatus.BLOCKED))

        val postBlockedTime = Instant.now()
        val postBlocked = LedgerMaster.readLedger()
        assertThat(postBlocked.user).isEqualTo(currentLedger.user)
        assertThat(postBlocked.versionInstallDates).isEqualTo(currentLedger.versionInstallDates)
        assertThat(postBlocked.allowedToPromote).isFalse
        assertThat(postBlocked.seenPromotions[ANI_MEME_PROMOTION_ID]?.result).isEqualTo(PromotionStatus.BLOCKED)
        assertThat(postBlocked.seenPromotions[ANI_MEME_PROMOTION_ID]?.id).isEqualTo(ANI_MEME_PROMOTION_ID)
        assertThat(postBlocked.seenPromotions[ANI_MEME_PROMOTION_ID]?.datePromoted).isBetween(
            beforePromotion,
            postBlockedTime
        )

        promotionCallback(PromotionResults(PromotionStatus.REJECTED))

        val postRejectedTime = Instant.now()
        val postRejected = LedgerMaster.readLedger()
        assertThat(postRejected.user).isEqualTo(currentLedger.user)
        assertThat(postRejected.versionInstallDates).isEqualTo(currentLedger.versionInstallDates)
        assertThat(postRejected.allowedToPromote).isTrue
        assertThat(postRejected.seenPromotions[ANI_MEME_PROMOTION_ID]?.result).isEqualTo(PromotionStatus.REJECTED)
        assertThat(postRejected.seenPromotions[ANI_MEME_PROMOTION_ID]?.id).isEqualTo(ANI_MEME_PROMOTION_ID)
        assertThat(postRejected.seenPromotions[ANI_MEME_PROMOTION_ID]?.datePromoted).isBetween(
            postBlockedTime,
            postRejectedTime
        )

        promotionCallback(PromotionResults(PromotionStatus.ACCEPTED))

        val postAcceptedTime = Instant.now()
        val postAccepted = LedgerMaster.readLedger()
        assertThat(postAccepted.user).isEqualTo(currentLedger.user)
        assertThat(postAccepted.versionInstallDates).isEqualTo(currentLedger.versionInstallDates)
        assertThat(postAccepted.allowedToPromote).isTrue
        assertThat(postAccepted.seenPromotions[ANI_MEME_PROMOTION_ID]?.result).isEqualTo(PromotionStatus.ACCEPTED)
        assertThat(postAccepted.seenPromotions[ANI_MEME_PROMOTION_ID]?.id).isEqualTo(ANI_MEME_PROMOTION_ID)
        assertThat(postAccepted.seenPromotions[ANI_MEME_PROMOTION_ID]?.datePromoted).isBetween(
            postRejectedTime,
            postAcceptedTime
        )
    }
}
