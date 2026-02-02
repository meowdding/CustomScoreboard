package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.CustomizationConfig
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.helpers.McClient

@ScoreboardElement
object ElementTitle : Element() {
    override fun getDisplay() = when {
        CustomizationConfig.useHypixelTitle -> McClient.scoreboardTitle?.align(CustomizationConfig.titleAlignment)
        CustomizationConfig.titleUseCustomText -> CustomizationConfig.titleText.formatTitle().map { it align CustomizationConfig.titleAlignment }
        else -> "§e§lSkyBlock" align CustomizationConfig.titleAlignment
    }

    override val configLine = "§e§lSkyBlock"
    override val id = "TITLE"
    override val group = ElementGroup.HEADER

    private fun String.formatTitle() = replace("&&", "§").split("\\n")
}
