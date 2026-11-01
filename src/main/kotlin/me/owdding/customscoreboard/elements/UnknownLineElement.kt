package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.core.UnknownLinesHandler
import me.owdding.customscoreboard.utils.ScoreboardElement

@ScoreboardElement
object UnknownLineElement : Element() {
    override fun getDisplay(): Any = UnknownLinesHandler.unknownLines

    override val configLine: String = "unknown_lines"
    override val id: String = "unknown_line"
}
