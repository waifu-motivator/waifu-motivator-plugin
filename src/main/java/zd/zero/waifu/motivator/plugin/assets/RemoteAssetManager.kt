package zd.zero.waifu.motivator.plugin.assets

import com.intellij.openapi.diagnostic.Logger
import zd.zero.waifu.motivator.plugin.platform.LifeCycleManager
import zd.zero.waifu.motivator.plugin.platform.UpdateAssetsListener
import zd.zero.waifu.motivator.plugin.tools.doOrElse
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.Optional.ofNullable

enum class Status {
    OK, BROKEN, UNKNOWN
}

interface HasStatus {
    var status: Status
}

abstract class RemoteAssetManager<T : AssetDefinition, U : Asset>(
    private val assetCategory: AssetCategory
) : HasStatus {
    private lateinit var remoteAndLocalAssets: List<T>
    private lateinit var localAssets: MutableSet<T>

    override var status = Status.UNKNOWN
    private val log = Logger.getInstance(this::class.java)

    init {
        initializeAssetCaches(AssetManager.resolveAssetUrl(assetCategory, "assets.json"))
        LifeCycleManager.registerUpdateListener(object : UpdateAssetsListener {
            override fun onRequestedUpdate() {
                initializeAssetCaches(
                    AssetManager.forceResolveAssetUrl(
                        assetCategory, "assets.json"
                    ),
                    breakOnFailure = false
                )
            }
        })
    }

    private fun initializeAssetCaches(assetFileUrl: Optional<String>, breakOnFailure: Boolean = true) {
        assetFileUrl
            .flatMap { assetUrl -> initializeRemoteAssets(assetUrl) }
            .doOrElse({ allAssetDefinitions ->
                status = Status.OK
                remoteAndLocalAssets = allAssetDefinitions
                localAssets = allAssetDefinitions.filter { asset ->
                    AssetManager.constructLocalAssetPath(assetCategory, asset.path)
                        .filter { Files.exists(it) }
                        .isPresent
                }.toSet().toMutableSet()
            }) {
                if (breakOnFailure) {
                    status = Status.BROKEN
                    remoteAndLocalAssets = listOf()
                    localAssets = mutableSetOf()
                }
            }
    }

    fun supplyAssetDefinitions(): List<T> =
        remoteAndLocalAssets

    fun supplyLocalAssetDefinitions(): Set<T> =
        localAssets

    fun supplyRemoteAssetDefinitions(): List<T> =
        remoteAndLocalAssets.filterNot { remoteOrLocalAsset ->
            localAssets.contains(remoteOrLocalAsset)
        }

    abstract fun convertToAsset(asset: T, assetUrl: String): U

    fun resolveAsset(asset: T): Optional<U> =
        AssetManager.resolveAssetUrl(assetCategory, asset.path)
            .map { assetUrl ->
                localAssets.add(asset)
                convertToAsset(asset, assetUrl)
            }

    private fun initializeRemoteAssets(assetUrl: String): Optional<List<T>> =
        try {
            readLocalFile(assetUrl)
                .flatMap {
                    convertToDefinitions(it)
                }
        } catch (e: Throwable) {
            log.error("Unable to initialize asset metadata.", e)
            Optional.empty()
        }

    private fun readLocalFile(assetUrl: String) =
        ofNullable(Files.readAllBytes(Paths.get(URI(assetUrl))))
            .map { String(it, Charsets.UTF_8) }

    abstract fun convertToDefinitions(defJson: String): Optional<List<T>>
}
