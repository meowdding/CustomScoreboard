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
object EventTrapper : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_BARN)

    override val configLine = "Dark Auction"


    private val formattedLines = mutableListOf<Component>()

    private val peltsRegex = ComponentRegex("Pelts: [\\d,.]+.*")
    private val mobLocationRegex = ComponentRegex("Tracker Mob Location:")


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWith {
            event.components.find(peltsRegex::matches)?.let(::add)
            addAll(event.components.sublistFromFirst(2, mobLocationRegex::matches))    
        }
    }

}
