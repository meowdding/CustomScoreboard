package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI

@ScoreboardElement
object ElementSowdust : Element(), NumberTrackingElement {
    override var previousAmount: Long = -1
    override var temporaryChangeDisplay: String? = null
    override val numberColor = "§2"

    override fun getDisplay(): Any {
        val sowdust = CurrencyAPI.sowdust
        checkDifference(sowdust)
        val line = sowdust.format() + temporaryChangeDisplay.orEmpty()
        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Sowdust", line, numberColor)
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GARDEN)
    override fun isLineActive() = CurrencyAPI.sowdust > 0

    override val configLine = "Sowdust"
    override val id = "SOWDUST"
}
