package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object IslandElement : Element() {
    override fun getDisplay() = "§7㋖ §a${LocationAPI.island}".withActions {
        hover(Text.of("Click to open the warp menu.", TextColor.GRAY))
        command = "/warp"
    }

    override fun showWhen() = LocationAPI.island != null

    override fun showIsland() = true

    override val configLine = "Island"
    override val id = "ISLAND"
    override val group = ElementGroup.HEADER
}
