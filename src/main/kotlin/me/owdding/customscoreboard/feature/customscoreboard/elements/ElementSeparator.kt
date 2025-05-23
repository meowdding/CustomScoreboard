package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine

@AutoElement(ElementGroup.SEPARATOR)
object ElementSeparator : Element() {
    override fun getDisplay() = ScoreboardLine("", isBlank = true)

    override val configLine = "------- Separator -------"
}
