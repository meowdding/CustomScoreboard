package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.CommonRegexes
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWith
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventGalatea : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GALATEA)

    override val configLine = "Galatea"


    private val remote = RemoteStrings.resolve()
    private val whisperRegex by remote.componentRegex("Whispers: [\\w,.]+.*")
    private val hotfRegex by remote.componentRegex("\\s*HOTF: [\\w,.]+.*")
    private val contestRegex by remote.componentRegex("Agatha's Contest.*")

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
