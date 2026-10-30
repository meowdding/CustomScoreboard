package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import me.owdding.lib.utils.strings.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object GardenEvent : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GARDEN)

    override val configLine = "Garden"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val pastingRegex by remote.componentRegex("\\s*(?:Barn )?Pasting: [\\d,.]+%?")
    private val cleanupRegex by remote.componentRegex("\\s*Cleanup: [\\d,.]*%?")

    override val regexes = setOf(pastingRegex, cleanupRegex)

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.newComponents, regexes)
    }

}
