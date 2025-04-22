package gay.j10a1n15.customscoreboard

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import com.teamresourceful.resourcefulconfig.api.loader.Configurator
import gay.j10a1n15.customscoreboard.config.CustomScoreboardKeybinds
import gay.j10a1n15.customscoreboard.config.MainConfig
import gay.j10a1n15.customscoreboard.feature.KeybindManager
import gay.j10a1n15.customscoreboard.feature.customscoreboard.CustomScoreboardBackground
import gay.j10a1n15.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import gay.j10a1n15.customscoreboard.feature.customscoreboard.elements.ElementArea
import gay.j10a1n15.customscoreboard.feature.customscoreboard.elements.ElementLobby
import gay.j10a1n15.customscoreboard.feature.customscoreboard.elements.ElementObjective
import gay.j10a1n15.customscoreboard.feature.customscoreboard.elements.ElementPurse
import gay.j10a1n15.customscoreboard.feature.customscoreboard.elements.ElementSlayer
import gay.j10a1n15.customscoreboard.feature.customscoreboard.elements.ElementTime
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventBroodmother
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventCarnival
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventDamage
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventDarkAuction
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventDojo
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventDungeons
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventEssence
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventFlightDuration
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventGarden
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventJacobsContest
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventKuudra
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventMagmaBoss
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventMining
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventNewYear
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventQueue
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventRedstone
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventRift
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventServerRestart
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventSpooky
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventTrapper
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventVoting
import gay.j10a1n15.customscoreboard.feature.customscoreboard.events.EventWinter
import gay.j10a1n15.customscoreboard.feature.updatechecker.UpdateChecker
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.LiteralCommandBuilder
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient

object Main : ModInitializer {

    const val VERSION = "@MOD_VERSION@"

    private val globalJob: Job = Job(null)
    val coroutineScope = CoroutineScope(
        CoroutineName("CustomScoreboard") + SupervisorJob(globalJob),
    )

    val configurator = Configurator("customscoreboard")

    override fun onInitialize() {
        MainConfig.register(configurator)

        SkyBlockAPI.eventBus.register(this)
        SkyBlockAPI.eventBus.register(CustomScoreboardRenderer)
        SkyBlockAPI.eventBus.register(UpdateChecker)
        SkyBlockAPI.eventBus.register(ElementArea)
        SkyBlockAPI.eventBus.register(ElementTime)
        SkyBlockAPI.eventBus.register(ElementLobby)
        SkyBlockAPI.eventBus.register(ElementObjective)
        SkyBlockAPI.eventBus.register(ElementSlayer)
        SkyBlockAPI.eventBus.register(EventVoting)
        SkyBlockAPI.eventBus.register(EventBroodmother)
        SkyBlockAPI.eventBus.register(EventEssence)
        SkyBlockAPI.eventBus.register(EventRedstone)
        SkyBlockAPI.eventBus.register(EventFlightDuration)
        SkyBlockAPI.eventBus.register(EventServerRestart)
        SkyBlockAPI.eventBus.register(EventDarkAuction)
        SkyBlockAPI.eventBus.register(EventNewYear)
        SkyBlockAPI.eventBus.register(EventGarden)
        SkyBlockAPI.eventBus.register(EventTrapper)
        SkyBlockAPI.eventBus.register(EventDojo)
        SkyBlockAPI.eventBus.register(EventWinter)
        SkyBlockAPI.eventBus.register(EventCarnival)
        SkyBlockAPI.eventBus.register(EventMagmaBoss)
        SkyBlockAPI.eventBus.register(EventSpooky)
        SkyBlockAPI.eventBus.register(EventDamage)
        SkyBlockAPI.eventBus.register(EventQueue)
        SkyBlockAPI.eventBus.register(EventMining)
        SkyBlockAPI.eventBus.register(EventKuudra)
        SkyBlockAPI.eventBus.register(EventDungeons)
        SkyBlockAPI.eventBus.register(EventRift)
        SkyBlockAPI.eventBus.register(EventJacobsContest)
        SkyBlockAPI.eventBus.register(ElementPurse)
        SkyBlockAPI.eventBus.register(CustomScoreboardKeybinds)
        SkyBlockAPI.eventBus.register(KeybindManager)

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
            override fun getFabricId(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("customscoreboard", "reload")

            override fun onResourceManagerReload(resourceManager: ResourceManager) {
                CustomScoreboardBackground.load()
            }

        })
    }

    @Subscription
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        val builder: (LiteralCommandBuilder.() -> Unit) = {
            callback {
                McClient.setScreen(ResourcefulConfigScreen.getFactory("customscoreboard").apply(null))
            }
        }

        event.register("customscoreboard") { builder() }
        event.register("csb") { builder() }
        event.register("cs") { builder() }
    }
}
