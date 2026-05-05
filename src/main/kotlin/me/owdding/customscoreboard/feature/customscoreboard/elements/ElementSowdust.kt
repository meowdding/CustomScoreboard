package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ElementSowdust : NumberTrackingElement(TextColor.DARK_GREEN) {
    override fun getDisplay(): Any {
        val sowdust = CurrencyAPI.sowdust
        checkDifference(sowdust)
        val line = Text.join(sowdust.format(), temporaryChangeDisplay)
        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Sowdust", line, numberColor)
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GARDEN)
    override fun isLineActive() = CurrencyAPI.sowdust > 0

    override val configLine = "Sowdust"
    override val id = "SOWDUST"
}
