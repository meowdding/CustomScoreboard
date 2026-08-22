package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer.currentIslandEvents
import me.owdding.customscoreboard.utils.ScoreboardElement

@ScoreboardElement
object EventsElement : Element() {
    override fun getDisplay() =
        if (LinesConfig.showAllActiveEvents) {
            val activeEvents = currentIslandEvents.mapNotNull { event ->
                event.event.getLines().takeIf { it.isNotEmpty() }
            }

            if (LinesConfig.separatorBetweenEvents) {
                activeEvents.flatMapIndexed { index, lines ->
                    if (index == 0) lines else listOf("") + lines
                }
            } else {
                activeEvents.flatten()
            }
        } else {
            currentIslandEvents.firstNotNullOfOrNull { event ->
                event.event.getLines().takeIf { it.isNotEmpty() }
            }
        }

    override val id = "EVENTS"
    override val configLine = "Events"
    override val configLineHover = listOf(
        "Please don't remove this element.",
        "It's used to display all kind of not-that-important information.",
        "See the events draggable list below.",
        "",
        "If I see a support question saying \"Why do I not have Dungeon lines\" and you removed this, I will cry.",
    )
}
