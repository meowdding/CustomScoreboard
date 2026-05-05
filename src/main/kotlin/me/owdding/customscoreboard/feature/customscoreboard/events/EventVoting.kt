package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventVoting : Event() {
    override fun getDisplay() = formattedLines

    override fun showWhen() = SkyBlockAreas.ELECTION_ROOM.inArea()

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.HUB)

    override val configLine = "Voting"

    override val configLineHover = listOf(
        "Shows the current voting event.",
        "Only visible in the election room.",
    )


    private val remote = RemoteStrings.resolve()
    private val titleRegex by remote.componentRegex("Year \\d+ Votes")
    private val subtitleRegex by remote.componentRegex("Waiting for|your vote\\.\\.\\.")
    private val candidatesRegex by remote.componentRegex("\\|{15}.+")
    private val regexes = listOf(titleRegex, subtitleRegex, candidatesRegex)

    private val formattedLines = mutableListOf<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, regexes)
    }
}
