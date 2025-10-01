package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement

@ScoreboardElement
object ElementFooter : Element() {
    override fun getDisplay() = MainConfig.footer.let { f ->
        if (f.useCustomText) f.text.formatFooter().map { it align f.alignment } else "§ewww.hypixel.net" align f.alignment
    }

    override val configLine = "§ewww.hypixel.net"
    override val id = "FOOTER"
    override val group = ElementGroup.FOOTER

    private fun String.formatFooter() = replace("&&", "§").split("\\n")
}
