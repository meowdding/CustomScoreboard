package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.helpers.McClient

@ScoreboardElement
object TitleElement : Element() {
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
