package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.CommonRegexes
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventJacobsContest : Event() {
    override fun getDisplay() = formattedLines

    override val configLine = "Jacob's Contest"


    private val contestRegex = ComponentRegex("Jacob's Contest")

    private var formattedLines = emptyList<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines = event.components.sublistFromFirst(3, contestRegex::matches)
            .filterNot(CommonRegexes.hypixelFooterRegex::matches)

    }
}
