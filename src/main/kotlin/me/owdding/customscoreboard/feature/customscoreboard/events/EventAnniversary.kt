package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent

@AutoElement
object EventAnniversary : Event() {
    override fun getDisplay() = formattedLine

    override val configLine = "Anniversary"


    private var formattedLine: Component? = null

    private val restartRegex by RemoteStrings.resolve().componentRegex("\\d+th Anniversary [\\d:]+")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLine = event.components.find(restartRegex::matches)
    }
}
