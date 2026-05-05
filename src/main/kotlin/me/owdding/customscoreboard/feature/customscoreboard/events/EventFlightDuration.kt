package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventFlightDuration : Event() {
    override fun getDisplay() = formattedLine

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND, SkyBlockIsland.GARDEN)

    override val configLine = "Flight Duration"


    private var formattedLine: Component? = null

    private val flightRegex by RemoteStrings.resolve().componentRegex("Flight Duration: [\\d:]+")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLine = event.components.find(flightRegex::matches)
    }
}
