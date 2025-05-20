package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup

@AutoElement(ElementGroup.SEPARATOR)
object ElementSeparator : Element() {
    override fun getDisplay() = ""

    override val configLine = "------- Separator -------"
}
