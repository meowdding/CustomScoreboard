package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.TextUtils.anyMatch
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@AutoElement
object EventSpooky : Event() {
    override fun getDisplay() = formattedLine

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND, SkyBlockIsland.GARDEN)

    override val configLine = "Spooky"


    private var formattedLine: Component? = null

    private val festivalRegex = ComponentRegex("Spooky Festival [\\d.,:]+")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLine = event.components.find(festivalRegex::matches)
    }
}
