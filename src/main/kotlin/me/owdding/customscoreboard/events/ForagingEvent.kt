package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.utils.CommonRegexes
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWith
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object ForagingEvent : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.GALATEA, SkyBlockIsland.TORRHUS_CANYON, SkyBlockIsland.SAFARI)

    override val configLine = "Foraging"


    private val remote = RemoteStrings.resolve()
    private val whisperRegex by remote.componentRegex(".*Whispers: [\\w,.]+.*")
    private val hotfRegex by remote.componentRegex("\\s*HOTF: [\\w,.]+.*")
    private val contestRegex by remote.componentRegex("(?:Agatha|Miria)'s Contest.*")
    private val capturedMobsRegex by remote.componentRegex("Captured Mobs: [\\d,m]+")

    private val formattedLines = mutableListOf<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWith {
            if (LinesConfig.showHypixelPowder) {
                event.newComponents.find(whisperRegex::matches)?.let(::add)
            }
            event.newComponents.find(hotfRegex::matches)?.let(::add)
            event.newComponents.find(capturedMobsRegex::matches)?.let(::add)
            addAll(event.newComponents.sublistFromFirst(3, contestRegex::matches))
            removeIf(CommonRegexes.hypixelFooterRegex::matches)
        }
    }
}
