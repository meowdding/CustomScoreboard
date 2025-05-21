package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventDojo : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.CRIMSON_ISLE)

    override fun showWhen() = SkyBlockArea.inAnyArea(SkyBlockAreas.DOJO_ARENA)

    override val configLine = "Dojo"


    private var formattedLines = mutableListOf<Component>()

    private val challengeRegex = ComponentRegex("Challenge: (?<challenge>.+)")
    private val difficultyRegex = ComponentRegex("Difficulty: (?<difficulty>.+)")
    private val pointsRegex = ComponentRegex("Points: [\\w.]+.*")
    private val timeRegex = ComponentRegex("Time: [\\w.]+.*")

    private val patterns = listOf(challengeRegex, difficultyRegex, pointsRegex, timeRegex)

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        formattedLines.addAll(
            event.components.filter { component ->
                patterns.any { it.matches(component) }
            },
        )
    }

}
