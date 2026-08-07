package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.core.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.api.profile.currency.PurseType
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object PurseElement : NumberTrackingElement(TextColor.GOLD) {

    override fun getDisplay(): Component {
        checkDifference(CurrencyAPI.purse.toLong())
        val line = Text.join(CurrencyAPI.purse.format(), temporaryChangeDisplay)
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
