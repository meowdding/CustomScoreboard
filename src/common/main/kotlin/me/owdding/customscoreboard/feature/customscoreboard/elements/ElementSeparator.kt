package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine
import me.owdding.customscoreboard.utils.ScoreboardElement

@AutoElement(ElementGroup.SEPARATOR)
@ScoreboardElement
object ElementSeparator : Element() {
    override fun getDisplay() = ScoreboardLine("", isBlank = true)

    override val configLine = "------- Separator -------"
    override val id = "SEPARATOR"
    override val canDuplicate: Boolean = true
}
