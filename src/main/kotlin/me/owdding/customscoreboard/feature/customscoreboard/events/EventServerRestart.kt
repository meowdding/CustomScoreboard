package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventServerRestart : Event() {
    override fun getDisplay() = formattedLine

    override val configLine = "Server Restart"


    private var formattedLine: Component? = null

    private val restartRegex = ComponentRegex("Server closing.*")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLine = event.components.find(restartRegex::matches)
    }
}
