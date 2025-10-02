package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.Utils.hasCookieActive
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.CurrencyAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI

@ScoreboardElement
object ElementBank : Element(), NumberTrackingElement {
    override var previousAmount: Long = -1
    override var temporaryChangeDisplay: String? = null
    override val numberColor = "§6"

    override fun getDisplay(): Any {
        checkDifference(CurrencyAPI.coopBank)
        val line = when (ProfileAPI.coop) {
            true -> "${CurrencyAPI.personalBank.format()}§7/§6${CurrencyAPI.coopBank.format()}"
            false -> CurrencyAPI.coopBank.format()
        } + temporaryChangeDisplay.orEmpty()

        val element = CustomScoreboardRenderer.formatNumberDisplayDisplay("Bank", line, numberColor)
        return if (!hasCookieActive()) element else element.withActions {
            hover = listOf("§7Click to open the bank")
            command = "/bank"
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override val configLine = "Bank"
    override val id = "BANK"
    override val configLineHover = listOf("Cannot be accurate enough,", "so it uses whats in the tablist")
}
