package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.Utils.replaceWith
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import me.owdding.lib.utils.strings.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object DarkAuctionEvent : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.DARK_AUCTION)

    override val configLine = "Dark Auction"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val startingInRegex by remote.componentRegex("Starting in: [\\w:,.\\s]+")
    private val timeLeftRegex by remote.componentRegex("Time Left: [\\w:,.\\s]+")
    val currentItemRegex by remote.componentRegex("Current Item:")

    override val regexes = setOf(startingInRegex, timeLeftRegex)

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWith {
            val lines = event.newComponents
            lines.find(startingInRegex::matches)?.let(::add)
            lines.find(timeLeftRegex::matches)?.let(::add)
            addAll(lines.sublistFromFirst(2, currentItemRegex::matches))
        }
    }

}
