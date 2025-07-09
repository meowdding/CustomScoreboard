package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.Utils.nextAfter
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@AutoElement
object EventGalatea : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GALATEA)

    override val configLine = "Galatea"


    private val whisperRegex = ComponentRegex("Whispers: [\\w,.]+.*")
    private val hotfRegex = ComponentRegex("\\s*HOTF: [\\w,.]+.*")
    private val contestRegex = ComponentRegex("Agatha's Contest.*")
    private val hypixelFooterRegex = "(?:www|alpha).hypixel.net".toRegex()

    private val formattedLines = mutableListOf<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()

        if (LinesConfig.showHypixelPowder) {
            whisperRegex.anyMatch(event.components) {
                formattedLines.add(it.component)
            }
        }
        hotfRegex.anyMatch(event.components) {
            formattedLines.add(it.component)
        }

        contestRegex.anyMatch(event.components) {
            formattedLines.add(it.component)
            event.components.nextAfter(it.component)?.let { formattedLines.add(it) }
            event.components.nextAfter(it.component, 2)?.let { formattedLines.add(it) }
        }

        formattedLines.removeIf { hypixelFooterRegex.matches(it.string) }
    }
}
