package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI

@ScoreboardElement
object ElementIsland : Element() {
    override fun getDisplay() = "§7㋖ §a${LocationAPI.island}".withActions {
        hover = listOf("§7Click to open the warp menu.")
        command = "/warp"
    }

    override fun showWhen() = LocationAPI.island != null

    override fun showIsland() = true

    override val configLine = "Island"
    override val id = "ISLAND"
    override val group = ElementGroup.HEADER
}
