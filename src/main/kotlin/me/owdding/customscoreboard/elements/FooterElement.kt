package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI

@ScoreboardElement
object FooterElement : Element() {
    override fun getDisplay() = with(CustomizationConfig) {
        val defaultFooter = if (LocationAPI.onAlpha) "§ealpha.hypixel.net" else "§ewww.hypixel.net"
        val customText = if (LocationAPI.onAlpha) alphaFooterText else footerText
        if (footerUseCustomText) customText.formatFooter().map { it align footerAlignment } else defaultFooter align footerAlignment
    }

    override val configLine = "§ewww.hypixel.net"
    override val id = "FOOTER"
    override val group = ElementGroup.FOOTER

    private fun String.formatFooter() = replace("&&", "§").split("\\n")
}
