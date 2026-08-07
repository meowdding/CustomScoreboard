package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.core.NumberTrackingElement
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.community.CommunityCenterAPI
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object BitsElement : NumberTrackingElement(TextColor.AQUA) {

    fun line() = (CurrencyAPI.bits.format() + if (LinesConfig.showBitsAvailable) "§7/§b${CommunityCenterAPI.bitsAvailable.format()}" else "")

    override fun getDisplay(): Any {
        val bits = CurrencyAPI.bits
        checkDifference(bits)
        val line = Text.join(line(), temporaryChangeDisplay)

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Bits", line, numberColor).withActions {
            hover = listOf("§7Click to open the SkyBlock menu to resync your bits.")
            command = "/sbmenu"
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_CATACOMBS, SkyBlockIsland.KUUDRA)
    override fun isLineActive() = CurrencyAPI.bits > 0 || (LinesConfig.showBitsAvailable && CommunityCenterAPI.bitsAvailable > 0)

    override val configLine = "Bits"
    override val id = "BITS"
}
