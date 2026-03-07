package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.CommonRegexes
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.TextUtils.isBlank
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent

@AutoElement
object EventJacobsContest : Event() {
    override fun getDisplay() = formattedLines

    override val configLine = "Jacob's Contest"


    private val contestRegex by RemoteStrings.resolve().componentRegex("Jacob's Contest")

    private var formattedLines = emptyList<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines = event.components.sublistFromFirst(4, contestRegex::matches)
            .filterNot(CommonRegexes.hypixelFooterRegex::matches).filterNot { it.isBlank() }

    }
}
