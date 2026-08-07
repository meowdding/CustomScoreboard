package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI

@ScoreboardElement
object PlayerCountElement : Element() {

    override fun getDisplay(): String {
        val current = LocationAPI.playerCount
        val max = LocationAPI.maxPlayercount

        val display = "${current}/${max}".takeIf { max != null } ?: current.toString()
        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Players", display, "§9")
    }

    override val configLine = "Player Count"
    override val id = "PLAYER_COUNT"
}
