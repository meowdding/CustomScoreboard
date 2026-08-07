package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.core.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object NorthStarsElement : NumberTrackingElement(TextColor.PINK) {

    override fun getDisplay(): Component {
        checkDifference(CurrencyAPI.northStars)
        val line = Text.join(CurrencyAPI.northStars.format(), temporaryChangeDisplay)

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("North Stars", line, numberColor)
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.JERRYS_WORKSHOP)
    override fun isLineActive() = CurrencyAPI.northStars > 0

    override val configLine = "North Stars"
    override val id = "NORTH_STARS"
}
