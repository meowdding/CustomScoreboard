package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement

@ScoreboardElement
object FooterElement : Element() {
    override fun getDisplay() = with(CustomizationConfig) {
        if (footerUseCustomText) footerText.formatFooter().map { it align footerAlignment } else "§ewww.hypixel.net" align footerAlignment
    }

    override val configLine = "§ewww.hypixel.net"
    override val id = "FOOTER"
    override val group = ElementGroup.FOOTER

    private fun String.formatFooter() = replace("&&", "§").split("\\n")
}
