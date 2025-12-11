package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.CommonRegexes
import me.owdding.customscoreboard.utils.Utils.replaceWith
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventGalatea : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GALATEA)

    override val configLine = "Galatea"


    private val whisperRegex = ComponentRegex("Whispers: [\\w,.]+.*")
    private val hotfRegex = ComponentRegex("\\s*HOTF: [\\w,.]+.*")
    private val contestRegex = ComponentRegex("Agatha's Contest.*")

    private val formattedLines = mutableListOf<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWith {
            if (LinesConfig.showHypixelPowder) {
                event.components.find(whisperRegex::matches)?.let(::add)
            }
            event.components.find(hotfRegex::matches)?.let(::add)
            addAll(event.components.sublistFromFirst(3, contestRegex::matches))
            removeIf(CommonRegexes.hypixelFooterRegex::matches)
        }
    }
}
