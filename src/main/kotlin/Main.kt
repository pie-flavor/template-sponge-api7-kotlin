@file:Suppress("UNUSED_PARAMETER")

package PS_TEMPLATE_BASE_PACKAGE_NAME

import flavor.pie.kludge.*
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.bstats.sponge.MetricsLite2
import org.spongepowered.api.asset.Asset
import org.spongepowered.api.asset.AssetId
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.plugin.Plugin
import java.nio.file.Path
import javax.inject.Inject

internal lateinit var config: Config

@Plugin(
    id = Main.ID,
    name = Main.NAME,
    version = Main.VERSION,
    authors = [Main.AUTHOR],
    description = Main.DESCRIPTION
)
class Main @Inject constructor(
    @DefaultConfig(sharedRoot = true) private val loader: ConfigurationLoader<CommentedConfigurationNode>,
    @DefaultConfig(sharedRoot = true) private val file: Path,
    @AssetId("default.conf") private val asset: Asset,
    metrics: MetricsLite2
) {

    companion object {
        const val ID = "PS_TEMPLATE_PLUGIN_ID"
        const val NAME = "PS_TEMPLATE_PLUGIN_NAME"
        const val VERSION = "PS_TEMPLATE_VERSION"
        const val AUTHOR = "PS_TEMPLATE_USER_NAME"
        const val DESCRIPTION = "PS_TEMPLATE_PLUGIN_DESCRIPTION"
    }

    @[Listener PublishedApi]
    internal fun onPreInit(e: GamePreInitializationEvent) {
        plugin = this
        config = loader.load().getValue(typeTokenOf<Config>())!!
    }

}