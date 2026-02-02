package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.CustomizationConfig
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement

@ScoreboardElement
object ElementFooter : Element() {
    override fun getDisplay() = with(CustomizationConfig) {
        if (footerUseCustomText) footerText.formatFooter().map { it align footerAlignment } else "§ewww.hypixel.net" align footerAlignment
    }

    override val configLine = "§ewww.hypixel.net"
    override val id = "FOOTER"
    override val group = ElementGroup.FOOTER

    private fun String.formatFooter() = replace("&&", "§").split("\\n")
}
