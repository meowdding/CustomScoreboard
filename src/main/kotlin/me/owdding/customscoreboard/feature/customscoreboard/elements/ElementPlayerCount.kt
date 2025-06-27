package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI

@AutoElement(ElementGroup.MIDDLE)
object ElementPlayerCount : Element() {

    override fun getDisplay(): String {
        val current = LocationAPI.playerCount
        val max = LocationAPI.maxPlayercount

        val display = "${current}/${max}".takeIf { max != null } ?: current.toString()
        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Players", display, "§9")
    }

    override val configLine = "Player Count"
}
