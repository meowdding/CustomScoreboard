package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventGarden : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GARDEN)

    override val configLine = "Garden"


    private val formattedLines = mutableListOf<Component>()

    private val pastingRegex = ComponentRegex("\\s*(?:Barn )?Pasting: [\\d,.]+%?")
    private val cleanupRegex = ComponentRegex("\\s*Cleanup: [\\d,.]*%?")

    private val regexes = listOf(pastingRegex, cleanupRegex)

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, regexes)
    }

}
