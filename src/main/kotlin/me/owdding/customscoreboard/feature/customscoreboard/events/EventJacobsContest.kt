package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.Utils.nextAfter
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@AutoElement
object EventJacobsContest : Event() {
    override fun getDisplay() = formattedLines

    override val configLine = "Jacob's Contest"


    private val contestRegex = ComponentRegex("Jacob's Contest")
    private val hypixelFooterRegex = "(?:www|alpha).hypixel.net".toRegex()

    private val formattedLines = mutableListOf<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()

        contestRegex.anyMatch(event.components) {
            formattedLines.add(it.component)
            event.components.nextAfter(it.component)?.let { formattedLines.add(it) }
            event.components.nextAfter(it.component, 2)?.let { formattedLines.add(it) }
            event.components.nextAfter(it.component, 3)?.let { formattedLines.add(it) }
        }

        formattedLines.removeIf { hypixelFooterRegex.matches(it.string) }

    }
}
