package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.Utils.nextAfter
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventDarkAuction : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.DARK_AUCTION)

    override val configLine = "Dark Auction"


    private var formattedLines = mutableListOf<Component>()

    private val startingInRegex = ComponentRegex("Starting in: [\\w:,.\\s]+")
    private val timeLeftRegex = ComponentRegex("Time Left: [\\w:,.\\s]+")
    private val currentItemRegex = ComponentRegex("Current Item:")


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        val lines = event.components
        val startingIn = lines.find { startingInRegex.matches(it) }
        val timeLeft = lines.find { timeLeftRegex.matches(it) }
        val currentItem = lines.find { currentItemRegex.matches(it) }
        val item = lines.nextAfter(currentItem)

        formattedLines = listOfNotNull(startingIn, timeLeft, currentItem, item).toMutableList()
    }

}
