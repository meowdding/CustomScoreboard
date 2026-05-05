package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.rift.RiftAPI
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@AutoElement
object EventRift : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override val configLine = "Rift"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val hotdogContestRegex by remote.componentRegex("Hot Dog Contest|Eaten: \\d+/\\d+")
    private val aveikxRegex by remote.componentRegex("Time spent sitting|with Ävaeìkx: .*")
    private val cluesRegex by remote.componentRegex("Clues: \\d+/\\d+")
    private val barryProtestRegex by remote.componentRegex("First Up|Find and talk with Barry")
    private val protestorsHandledRegex by remote.componentRegex("Protestors handled: \\d+/\\d+")

    private val patterns = listOf(hotdogContestRegex, aveikxRegex, cluesRegex, barryProtestRegex, protestorsHandledRegex)

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)

        if (
            SkyBlockArea.inAnyArea(
                SkyBlockAreas.STILLGORE_CHATEAU,
                SkyBlockAreas.OUBLIETTE,
                SkyBlockAreas.PHOTON_PATHWAY,
                SkyBlockAreas.FAIRYLOSOPHER_TOWER,
            )
        ) {
            val component = Text.of("Effigies: ") {
                RiftAPI.effieges.forEach { effigy ->
                    append("⧯") {
                        this.color = if (effigy.enabled) TextColor.RED else TextColor.GRAY
                    }
                }
            }
            formattedLines.add(component)
        }
    }
}
