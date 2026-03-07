package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventDojo : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.CRIMSON_ISLE)

    override fun showWhen() = SkyBlockArea.inAnyArea(SkyBlockAreas.DOJO_ARENA)

    override val configLine = "Dojo"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val challengeRegex by remote.componentRegex("Challenge: (?<challenge>.+)")
    private val difficultyRegex by remote.componentRegex("Difficulty: (?<difficulty>.+)")
    private val pointsRegex by remote.componentRegex("Points: -?[\\w.]+.*")
    private val timeRegex by remote.componentRegex("Time: [\\w.]+.*")

    private val patterns = listOf(challengeRegex, difficultyRegex, pointsRegex, timeRegex)

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }

}
