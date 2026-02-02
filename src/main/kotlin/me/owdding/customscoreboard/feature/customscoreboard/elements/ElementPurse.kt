package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.api.profile.currency.PurseType

@ScoreboardElement
object ElementPurse : NumberTrackingElement("§6") {

    override fun getDisplay(): String {
        checkDifference(CurrencyAPI.purse.toLong())
        val line = CurrencyAPI.purse.format() + temporaryChangeDisplay.orEmpty()
        return CustomScoreboardRenderer.formatNumberDisplayDisplay(
            if (CurrencyAPI.purseType == PurseType.PIGGY && LinesConfig.showPiggy) "Piggy" else "Purse",
            line,
            numberColor,
        )
    }

    override fun showWhen() = !(LinesConfig.hidePurseInDungeons && SkyBlockIsland.THE_CATACOMBS.inIsland())
    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)
    override fun isLineActive() = CurrencyAPI.purse > 0

    override val configLine = "Purse"
    override val id = "PURSE"
}
