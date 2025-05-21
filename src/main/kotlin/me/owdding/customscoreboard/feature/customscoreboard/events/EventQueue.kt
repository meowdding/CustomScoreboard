package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventQueue : Event() {
    override fun getDisplay() = formattedLines

    override val configLine = "Queue"


    private var formattedLines = mutableListOf<Component>()

    private val titleRegex = ComponentRegex("Queued:.*")
    private val tierRegex = ComponentRegex("Tier: .*")
    private val positionRegex = ComponentRegex("Position: #\\d+ Since: .*")

    private val patterns = listOf(titleRegex, tierRegex, positionRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        formattedLines.addAll(
            event.components.filter { component ->
                patterns.any { it.matches(component) }
            },
        )
    }
}
