package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.TextUtils.anyMatch
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@AutoElement
object EventNewYear : Event() {
    override fun getDisplay() = formattedLine

    override val configLine = "New Year"


    private var formattedLine: Component? = null

    private val newYearRegex = ComponentRegex("New Year Event! [\\d:]*")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (newYearRegex.regex().anyMatch(event.removed)) {
            formattedLine = null
        }

        newYearRegex.anyMatch(event.addedComponents) {
            formattedLine = it.component
        }

    }
}
