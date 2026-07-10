package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.data.MayorPerks
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ElementKernels : NumberTrackingElement(TextColor.YELLOW) {
    override fun getDisplay(): Any {
        val kernels = CurrencyAPI.kernels
        checkDifference(kernels)
        val line = Text.join(kernels.format(), temporaryChangeDisplay)
        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Kernels", line, numberColor)
    }

    override fun showWhen(): Boolean = MayorPerks.GRAND_FEAST.active
    override fun isLineActive() = CurrencyAPI.kernels > 0

    override val configLine = "Kernels"
    override val id = "KERNELS"
}
