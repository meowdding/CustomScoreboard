package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.helpers.McClient

@ScoreboardElement
object ElementTitle : Element() {
    override fun getDisplay() = when {
        LinesConfig.useHypixelTitle -> McClient.scoreboardTitle?.align(MainConfig.title.alignment)
        MainConfig.title.useCustomText -> MainConfig.title.text.formatTitle().map { it align MainConfig.title.alignment }
        else -> "§e§lSkyBlock" align MainConfig.title.alignment
    }

    override val configLine = "§e§lSkyBlock"
    override val id = "TITLE"
    override val group = ElementGroup.HEADER

    private fun String.formatTitle() = replace("&&", "§").split("\\n")
}
