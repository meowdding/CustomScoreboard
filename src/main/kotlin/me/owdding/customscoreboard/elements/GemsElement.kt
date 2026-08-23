package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.core.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.extensions.shorten
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object GemsElement : NumberTrackingElement(TextColor.GREEN) {

    override fun format(number: Number): String {
        return if (LinesConfig.gemsAlwaysCompact) number.shorten()
        else super.format(number)
    }

    override fun getDisplay(): Component {
        checkDifference(CurrencyAPI.gems)
        val line = Text.join(format(CurrencyAPI.gems), temporaryChangeDisplay)

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Gems", line, numberColor)
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT, SkyBlockIsland.THE_CATACOMBS, SkyBlockIsland.KUUDRA)
    override fun isLineActive() = CurrencyAPI.gems > 0

    override val configLine = "Gems"
    override val id = "GEMS"
}
