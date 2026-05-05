package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventDungeons : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_CATACOMBS)

    override val configLine = "Dungeons"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val m7dragonsRegex by remote.componentRegex("No Alive Dragons|- [\\w\\s]+Dragon [\\w,.]+❤")
    private val autoCloseRegex by remote.componentRegex("Auto-closing in: [\\d+:,.]+")
    private val startingInRegex by remote.componentRegex("Starting in: [\\d+:,.]+")
    private val keyRegex by remote.componentRegex("Keys: ■ [✗✓] ■ .x")
    private val timeElapsedRegex by remote.componentRegex("Time Elapsed: (?<time>(\\w+[ydhms] ?)+)")
    private val clearedRegex by remote.componentRegex("Cleared: (?<percent>[\\w,.]+)% \\((?<score>[\\w,.]+)\\)")
    private val soloRegex by remote.componentRegex("Solo")
    private val teammatesRegex by remote.componentRegex("(?<classAbbv>\\[\\w]) (?<username>\\w{2,16}) ((?<classLevel>\\[Lvl?(?<level>[\\w,.]+)?]?)|(?<health>[\\w,.]+).?)")
    private val f3guardianRegex by remote.componentRegex(" - (?:Healthy|Reinforced|Laser|Chaos) [\\w,.]*❤?")

    private val patterns = listOf(
        m7dragonsRegex,
        autoCloseRegex,
        startingInRegex,
        keyRegex,
        timeElapsedRegex,
        clearedRegex,
        soloRegex,
        teammatesRegex,
        f3guardianRegex,
    )


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }
}
