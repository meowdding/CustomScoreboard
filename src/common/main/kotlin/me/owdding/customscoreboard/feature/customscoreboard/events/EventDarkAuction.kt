package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.Utils.nextAfter
import me.owdding.customscoreboard.utils.Utils.replaceWith
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventDarkAuction : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.DARK_AUCTION)

    override val configLine = "Dark Auction"


    private val formattedLines = mutableListOf<Component>()

    private val startingInRegex = ComponentRegex("Starting in: [\\w:,.\\s]+")
    private val timeLeftRegex = ComponentRegex("Time Left: [\\w:,.\\s]+")
    private val currentItemRegex = ComponentRegex("Current Item:")


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWith {
            val lines = event.components
            lines.find(startingInRegex::matches)?.let(::add)
            lines.find(timeLeftRegex::matches)?.let(::add)
            addAll(lines.sublistFromFirst(2, currentItemRegex::matches))
        }
    }

}
