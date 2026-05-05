package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ElementCopper : NumberTrackingElement(TextColor.RED) {

    override fun getDisplay(): Any {
        checkDifference(CurrencyAPI.copper)
        val line = Text.join(CurrencyAPI.copper.format(), temporaryChangeDisplay)

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Copper", line, numberColor).withActions {
            hover = listOf("§7Click to teleport to your barn.")
            command = "/tptoplot barn"
        }
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GARDEN)
    override fun isLineActive() = CurrencyAPI.copper > 0

    override val configLine = "Copper"
    override val id = "COPPER"
}
