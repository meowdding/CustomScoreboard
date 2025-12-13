package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.CurrencyAPI
import tech.thatgravyboat.skyblockapi.api.profile.community.CommunityCenterAPI

@ScoreboardElement
object ElementBits : Element(), NumberTrackingElement {
    override var previousAmount: Long = -1
    override var temporaryChangeDisplay: String? = null
    override val numberColor = "§b"

    override fun getDisplay(): Any {
        val bits = CurrencyAPI.bits
        checkDifference(bits)
        val line = (bits.format() + if (LinesConfig.showBitsAvailable) "§7/§b${CommunityCenterAPI.bitsAvailable.format()}" else "") +
            temporaryChangeDisplay.orEmpty()

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Bits", line, numberColor).withActions {
            hover = listOf("§7Click to open the SkyBlock menu to resync your bits.")
            command = "/sbmenu"
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_CATACOMBS)
    override fun isLineActive() = CurrencyAPI.bits > 0 || (LinesConfig.showBitsAvailable && CommunityCenterAPI.bitsAvailable > 0)

    override val configLine = "Bits"
    override val id = "BITS"
}
