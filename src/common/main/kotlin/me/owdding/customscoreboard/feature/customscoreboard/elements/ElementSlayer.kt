package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.Utils.replaceWith
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@ScoreboardElement
object ElementSlayer : Element() {
    override fun getDisplay() = formattedLines

    override val configLine = "Slayer"
    override val id = "SLAYER"


    private val slayerQuestRegex = ComponentRegex("Slayer Quest")

    private var formattedLines = emptyList<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines = event.components.sublistFromFirst(3, slayerQuestRegex::matches)
    }
}
