package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent

@AutoElement
object EventQueue : Event() {
    override fun getDisplay() = formattedLines

    override val configLine = "Queue"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val titleRegex by remote.componentRegex("Queued:.*")
    private val tierRegex by remote.componentRegex("Tier: .*")
    private val positionRegex by remote.componentRegex("Position: #\\d+ Since: .*")

    private val patterns = listOf(titleRegex, tierRegex, positionRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }
}
