package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.config.category.CustomizationConfig.alphaFooterText
import me.owdding.customscoreboard.config.category.CustomizationConfig.footerAlignment
import me.owdding.customscoreboard.config.category.CustomizationConfig.footerText
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.rendering.text.serialization.TagComponentSerialization
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object FooterElement : Element() {
    private val footerComponent = Text.of("www.hypixel.net", TextColor.YELLOW)
    private val alphaFooterComponent = Text.of("alpha.hypixel.net", TextColor.YELLOW)
    override fun getDisplay() = with(CustomizationConfig) {
        val defaultFooter = if (LocationAPI.onAlpha) alphaFooterComponent else footerComponent
        if (footerUseCustomText) footer() else defaultFooter align footerAlignment
    }

    override val configLine = "§ewww.hypixel.net"
    override val id = "FOOTER"
    override val group = ElementGroup.FOOTER

    private fun footer() = (if (LocationAPI.onAlpha) alphaFooterText else footerText).map { TagComponentSerialization.deserialize(it) align footerAlignment }
}
