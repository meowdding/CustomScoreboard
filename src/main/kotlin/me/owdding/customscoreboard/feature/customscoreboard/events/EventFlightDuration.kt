package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.utils.TextUtils.anyMatch
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@Module
object EventFlightDuration : Event() {
    override fun getDisplay() = formattedLine

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND, SkyBlockIsland.GARDEN)

    override val configLine = "Flight Duration"


    private var formattedLine: Component? = null

    private val flightRegex = ComponentRegex("Flight Duration: [\\d:]+")

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (flightRegex.regex().anyMatch(event.removed)) {
            formattedLine = null
        }

        flightRegex.anyMatch(event.addedComponents) {
            formattedLine = it.component
        }

    }
}
