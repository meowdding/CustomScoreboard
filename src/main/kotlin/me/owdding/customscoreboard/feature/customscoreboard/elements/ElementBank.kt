package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.Utils.hasCookieActive
import me.owdding.lib.extensions.shorten
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.CurrencyAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI

@ScoreboardElement
object ElementBank : NumberTrackingElement("§6") {

    override fun format(number: Number): String {
        return if (LinesConfig.bankAlwaysCompact) number.shorten()
        else super.format(number)
    }

    fun line() = when (ProfileAPI.coop) {
        true -> when (LinesConfig.coopBankLayout) {
            CoopBankLayout.PERSONAL_COOP -> "${format(CurrencyAPI.personalBank)}§7/§6${format(CurrencyAPI.coopBank)}"
            CoopBankLayout.COOP_PERSONAL -> "${format(CurrencyAPI.coopBank)}§7/§6${format(CurrencyAPI.personalBank)}"
            CoopBankLayout.COMBINED -> format(CurrencyAPI.personalBank + CurrencyAPI.coopBank)
        }

        false -> format(CurrencyAPI.coopBank)
    }


    override fun getDisplay(): Any {
        checkDifference(CurrencyAPI.coopBank)
        val line = line() + temporaryChangeDisplay.orEmpty()

        val element = CustomScoreboardRenderer.formatNumberDisplayDisplay("Bank", line, numberColor)
        return if (!hasCookieActive()) element else element.withActions {
            hover = listOf("§7Click to open the bank")
            command = "/bank"
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)
    override fun isLineActive() = CurrencyAPI.coopBank > 0 || CurrencyAPI.personalBank > 0

    override val configLine = "Bank"
    override val id = "BANK"
    override val configLineHover = listOf("Cannot be accurate enough,", "so it uses whats in the tablist")

    enum class CoopBankLayout(val display: String) {
        PERSONAL_COOP("Personal/Coop"),
        COOP_PERSONAL("Coop/Personal"),
        COMBINED("Combined Value");

        override fun toString() = display
    }
}
