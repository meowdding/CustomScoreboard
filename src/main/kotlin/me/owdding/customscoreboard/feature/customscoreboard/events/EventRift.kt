package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventRift : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override val configLine = "Rift"


    private val formattedLines = mutableListOf<Component>()

    // TODO: fix RiftAPI.effieges
    private val effigiesRegex = ComponentRegex("Effigies: ⧯*")
    private val hotdogContestRegex = ComponentRegex("Hot Dog Contest|Eaten: \\d+/\\d+")
    private val aveikxRegex = ComponentRegex("Time spent sitting|with Ävaeìkx: .*")
    private val cluesRegex = ComponentRegex("Clues: \\d+/\\d+")
    private val barryProtestRegex = ComponentRegex("First Up|Find and talk with Barry")
    private val protestorsHandledRegex = ComponentRegex("Protestors handled: \\d+/\\d+")

    private val patterns = listOf(effigiesRegex, hotdogContestRegex, aveikxRegex, cluesRegex, barryProtestRegex, protestorsHandledRegex)

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }
}
