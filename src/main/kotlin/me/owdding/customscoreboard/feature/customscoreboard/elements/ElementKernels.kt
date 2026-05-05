package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI

@ScoreboardElement
object ElementKernels : NumberTrackingElement("§e") {
    override fun getDisplay(): Any {
        val kernels = CurrencyAPI.kernels
        checkDifference(kernels)
        val line = kernels.format() + temporaryChangeDisplay.orEmpty()
        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Kernels", line, numberColor)
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GARDEN)
    override fun isLineActive() = CurrencyAPI.kernels > 0

    override val configLine = "Kernels"
    override val id = "KERNELS"
}
