package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@Module
@AutoElement
object EventDungeons : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_CATACOMBS)

    override val configLine = "Dungeons"


    private var formattedLines = mutableListOf<Component>()

    private val m7dragonsRegex = ComponentRegex("No Alive Dragons|- [\\w\\s]+Dragon [\\w,.]+❤")
    private val autoCloseRegex = ComponentRegex("Auto-closing in: [\\d+:,.]+")
    private val startingInRegex = ComponentRegex("Starting in: [\\d+:,.]+")
    private val keyRegex = ComponentRegex("Keys: ■ [✗✓] ■ .x")
    private val timeElapsedRegex = ComponentRegex("Time Elapsed: (?<time>(\\w+[ydhms] ?)+)")
    private val clearedRegex = ComponentRegex("Cleared: (?<percent>[\\w,.]+)% \\((?<score>[\\w,.]+)\\)")
    private val soloRegex = ComponentRegex("Solo")
    private val teammatesRegex =
        ComponentRegex("(?<classAbbv>\\[\\w]) (?<username>\\w{2,16}) ((?<classLevel>\\[Lvl?(?<level>[\\w,.]+)?]?)|(?<health>[\\w,.]+).?)")
    private val f3guardianRegex = ComponentRegex(" - (?:Healthy|Reinforced|Laser|Chaos) [\\w,.]*❤?")

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


    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        formattedLines.addAll(
            event.components.filter { component ->
                patterns.any { it.matches(component) }
            },
        )
    }
}
