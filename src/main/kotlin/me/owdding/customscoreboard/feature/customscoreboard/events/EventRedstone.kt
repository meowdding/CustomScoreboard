package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventRedstone : Event() {
    override fun getDisplay() = formattedLine

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND)

    override val configLine = "Redstone"


    private var formattedLine: Component? = null

    private val redstoneRegex by RemoteStrings.resolve().componentRegex(" ⚡ Redstone: [\\d.,]+%")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLine = event.components.find(redstoneRegex::matches)
    }
}
