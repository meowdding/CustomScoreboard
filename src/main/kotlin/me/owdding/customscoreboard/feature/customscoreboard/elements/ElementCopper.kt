package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.CurrencyAPI

@AutoElement
object ElementCopper : Element(), NumberTrackingElement {
    override var previousAmount: Long = -1
    override var temporaryChangeDisplay: String? = null
    override val numberColor = "§c"

    override fun getDisplay(): Any {
        checkDifference(CurrencyAPI.copper)
        val line = CurrencyAPI.copper.format() + temporaryChangeDisplay.orEmpty()

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Copper", line, numberColor).withActions {
            hover = listOf("§7Click to teleport to your barn.")
            command = "/tptoplot barn"
        }
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GARDEN)

    override val configLine = "Copper"
}
