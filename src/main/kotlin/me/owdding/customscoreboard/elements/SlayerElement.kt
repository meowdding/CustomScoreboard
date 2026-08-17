package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.utils.LocationData
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.TextUtils.trim
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import tech.thatgravyboat.skyblockapi.api.area.slayer.SlayerAPI
import tech.thatgravyboat.skyblockapi.api.area.slayer.SlayerType
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.OnlyWidget
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidgetChangeEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@Module
@ScoreboardElement
object SlayerElement : Element() {
    override fun getDisplay() = formattedLines

    override val configLine = "Slayer"
    override val id = "SLAYER"

    override fun showWhen(): Boolean = !LinesConfig.hideSlayerOutsideSlayerAreas || isInSlayerRegion()

    private val slayerLocations = mapOf(
        SlayerType.REVENANT_HORROR to LocationData(
            areas = setOf(SkyBlockAreas.GRAVEYARD, SkyBlockArea("Crypts")),
        ),
        SlayerType.TARANTULA_BROODFATHER to LocationData(
            areas = setOf(SkyBlockAreas.BURNING_DESERT),
            islands = setOf(SkyBlockIsland.SPIDERS_DEN),
            islandBasedAABBs = mapOf(SkyBlockIsland.CRIMSON_ISLE to setOf(AABB(-540.0, 80.0, -646.0, -445.0, 110.0, -895.0))),
        ),
        SlayerType.SVEN_PACKMASTER to LocationData(
            areas = setOf(SkyBlockAreas.RUINS, SkyBlockAreas.SPIRIT_CAVE, SkyBlockAreas.SOUL_CAVE, SkyBlockAreas.HOWLING_CAVE),
        ),
        SlayerType.VOIDGLOOM_SERAPH to LocationData(
            islands = setOf(SkyBlockIsland.THE_END),
        ),
        SlayerType.INFERNO_DEMONLORD to LocationData(
            areas = setOf(SkyBlockAreas.SMOLDERING_TOMB, SkyBlockAreas.THE_WASTELAND, SkyBlockArea("Stronghold")),
        ),
        SlayerType.RIFTSTALKER_BLOODFIEND to LocationData(
            areas = setOf(SkyBlockAreas.STILLGORE_CHATEAU, SkyBlockAreas.PHOTON_PATHWAY, SkyBlockAreas.OUBLIETTE, SkyBlockAreas.FAIRYLOSOPHER_TOWER),
        ),
    )

    fun isInSlayerRegion(): Boolean = slayerLocations[SlayerAPI.type]?.inLocation() ?: slayerLocations.any { it.value.inLocation() }

    private val slayerQuestRegex by RemoteStrings.resolve().componentRegex("Slayer(?::| Quest)")

    private var formattedLines = emptyList<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines = event.newComponents.sublistFromFirst(3, slayerQuestRegex::matches)
    }

    @Subscription
    @OnlyWidget(TabWidget.SLAYER)
    fun onTabWidget(event: TabWidgetChangeEvent) {
        if (formattedLines.isNotEmpty()) return
        formattedLines = event.newComponents.sublistFromFirst(3, slayerQuestRegex::matches).map { it.trim() }
    }
}
