package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.TextUtils.anyMatch
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@AutoElement
object EventRedstone : Event() {
    override fun getDisplay() = formattedLine

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND)

    override val configLine = "Redstone"


    private var formattedLine: Component? = null

    private val redstoneRegex = ComponentRegex(" ⚡ Redstone: [\\d.,]+%")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (redstoneRegex.regex().anyMatch(event.removed)) {
            formattedLine = null
        }

        redstoneRegex.anyMatch(event.addedComponents) {
            formattedLine = it.component
        }

    }
}
