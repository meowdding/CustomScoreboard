package me.owdding.customscoreboard

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigUI
import com.teamresourceful.resourcefulconfig.api.loader.Configurator
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import me.owdding.customscoreboard.config.CUSTOM_DRAGGABLE_RENDERER
import me.owdding.customscoreboard.config.CustomDraggableList
import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardBackground
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardEditor
import me.owdding.customscoreboard.feature.customscoreboard.TabWidgetHelper
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import me.owdding.customscoreboard.generated.CustomScoreboardModules
import me.owdding.customscoreboard.generated.CustomScoreboardScoreboardElements
import me.owdding.customscoreboard.utils.RegisterCustomScoreboardCommandEvent
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import me.owdding.ktmodules.Module
import me.owdding.lib.utils.MeowddingLogger
import me.owdding.lib.utils.MeowddingUpdateChecker
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.hover
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.url

@Module
object Main : ClientModInitializer, MeowddingLogger by MeowddingLogger.autoResolve() {

    val SELF = FabricLoader.getInstance().getModContainer("customscoreboard").get()
    val MOD_ID: String = SELF.metadata.id
    val VERSION: String = SELF.metadata.version.friendlyString

    private val globalJob: Job = Job(null)
    val coroutineScope = CoroutineScope(CoroutineName("CustomScoreboard") + SupervisorJob(globalJob))

    val configurator = Configurator("customscoreboard")
    val config = MainConfig.register(configurator)

    private val allScoreboardElements = mutableListOf<Element>()
    val allPossibleScoreboardElements get() = allScoreboardElements + TabWidgetHelper.tablistLineCache

    override fun onInitializeClient() {
        ResourcefulConfigUI.registerElementRenderer(CUSTOM_DRAGGABLE_RENDERER, ::CustomDraggableList)

        CustomScoreboardModules.init { SkyBlockAPI.eventBus.register(it) }
        CustomScoreboardScoreboardElements.init { allScoreboardElements.add(it as Element) }

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
            object : SimpleSynchronousResourceReloadListener {
                override fun getFabricId(): Identifier = Identifier.fromNamespaceAndPath("customscoreboard", "reload")

                override fun onResourceManagerReload(resourceManager: ResourceManager) {
                    CustomScoreboardBackground.load()
                }
            },
        )

        MeowddingUpdateChecker("fpb5uaJt", SELF) { link, current, new ->
            if (!MainConfig.updateNotification) return@MeowddingUpdateChecker

            fun MutableComponent.withLink() = this.apply {
                this.url = link
                this.hover = Text.of(link).withColor(TextColor.GRAY)
            }

            McClient.runNextTick {
                Text.of().send()
                Text.join(
                    "New version found! (",
                    Text.of(current).withColor(TextColor.RED),
                    Text.of(" -> ").withColor(TextColor.GRAY),
                    Text.of(new).withColor(TextColor.GREEN),
                    ")",
                ).withLink().sendWithPrefix()
                Text.of("Click to download.").withLink().sendWithPrefix()
                Text.of().send()
            }
        }
    }

    fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)

    @Subscription
    fun onCommand(event: RegisterCommandsEvent) = RegisterCustomScoreboardCommandEvent(event).post(SkyBlockAPI.eventBus)

    @Subscription
    fun onRegisterCommands(event: RegisterCustomScoreboardCommandEvent) {
        event.registerBaseCallback {
            McClient.setScreenAsync { ResourcefulConfigScreen.getFactory("customscoreboard").apply(null) }
        }

        event.registerWithCallback("version") {
            Text.of("Version: $VERSION").withColor(TextColor.GRAY).sendWithPrefix()
        }

        event.registerWithCallback("editor") {
            McClient.setScreenAsync { CustomScoreboardEditor() }
        }
    }
}
