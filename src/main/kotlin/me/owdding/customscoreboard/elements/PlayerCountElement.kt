package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomPrefix
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object PlayerCountElement : Element() {

    override fun getDisplay(): Component {
        val current = LocationAPI.playerCount
        val max = LocationAPI.maxPlayercount

        val display = "${current}/${max}".takeIf { max != null } ?: current.toString()
        return CustomScoreboardRenderer.formatNumberDisplayDisplay(CustomPrefix.PLAYER_COUNT, display, TextColor.BLUE)
    }

    override val configLine = "Player Count"
    override val id = "PLAYER_COUNT"
}
