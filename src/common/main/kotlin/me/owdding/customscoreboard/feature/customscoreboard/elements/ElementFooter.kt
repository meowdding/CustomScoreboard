package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.align

@AutoElement(ElementGroup.FOOTER)
object ElementFooter : Element() {
    override fun getDisplay() = MainConfig.footer.let { f ->
        if (f.useCustomText) f.text.formatFooter().map { it align f.alignment } else "§ewww.hypixel.net" align f.alignment
    }

    override val configLine = "§ewww.hypixel.net"

    fun String.formatFooter() = replace("&&", "§").split("\\n")
}
