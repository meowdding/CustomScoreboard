package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI

@AutoElement(ElementGroup.HEADER)
object ElementIsland : Element() {
    override fun getDisplay() = "§7㋖ §a${LocationAPI.island}".withActions {
        hover = listOf("§7Click to open the warp menu.")
        command = "/warp"
    }

    override fun showWhen() = LocationAPI.island != null

    override fun showIsland() = true

    override val configLine = "Island"
}
