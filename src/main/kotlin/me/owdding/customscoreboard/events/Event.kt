package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.core.ScoreboardLine
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.elements.Element
import me.owdding.customscoreboard.utils.TextUtils.removePrefix


abstract class Event : Element() {
    override val id = "USELESS"

    override fun getLines(): List<ScoreboardLine> = super.getLines().map { line ->
        (line.component.removePrefix("Event: ") align line.alignment).copy(actions = line.actions)
    }
}
