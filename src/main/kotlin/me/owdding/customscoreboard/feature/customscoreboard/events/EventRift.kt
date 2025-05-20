package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.TextUtils.toComponent
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.rift.RiftAPI
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@Module
@AutoElement
object EventRift : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override val configLine = "Rift"


    private var formattedLines = mutableListOf<Component>()

    private val hotdogContestRegex = ComponentRegex("Hot Dog Contest|Eaten: \\d+/\\d+")
    private val aveikxRegex = ComponentRegex("Time spent sitting|with Ävaeìkx: .*")
    private val cluesRegex = ComponentRegex("Clues: \\d+/\\d+")
    private val barryProtestRegex = ComponentRegex("First Up|Find and talk with Barry")
    private val protestorsHandledRegex = ComponentRegex("Protestors handled: \\d+/\\d+")

    private val patterns = listOf(hotdogContestRegex, aveikxRegex, cluesRegex, barryProtestRegex, protestorsHandledRegex)

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        formattedLines.addAll(
            event.components.filter { component ->
                patterns.any { it.matches(component) }
            },
        )

        if (
            SkyBlockArea.inAnyArea(
                SkyBlockAreas.STILLGORE_CHATEAU,
                SkyBlockAreas.OUBLIETTE,
                SkyBlockAreas.PHOTON_PATHWAY,
                SkyBlockAreas.FAIRYLOSOPHER_TOWER,
            )
        ) {
            val string = buildString {
                append("Effigies: ")
                RiftAPI.effieges.map { effigy ->
                    append("§")
                    append(
                        if (effigy.enabled) "c"
                        else "7",
                    )
                    append("⧯")
                }
            }
            formattedLines.add(string.toComponent())
        }
    }
}
