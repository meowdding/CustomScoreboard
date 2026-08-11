package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWith
import me.owdding.lib.displays.Alignment
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@AutoElement
object MiningEvent : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() =
        SkyBlockIsland.inAnyIsland(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.CRYSTAL_HOLLOWS, SkyBlockIsland.MINESHAFT)

    override val configLine = "Mining"


    private val formattedLines = mutableListOf<Any>()

    private val remote = RemoteStrings.resolve()
    private val powderRegex by remote.componentRegex("᠅ (?:Gemstone|Mithril|Glacite)(?: Powder)?.*")
    private val eventRegex by remote.componentRegex("Event: .*")
    private val eventZoneRegex by remote.componentRegex("Zone: .*")
    private val raffleUselessRegex by remote.componentRegex("Find tickets on the|ground and bring them|to the raffle box")
    private val raffleTicketsRegex by remote.componentRegex("Tickets: \\d+ \\([\\d.,]+%\\)")
    private val rafflePoolRegex by remote.componentRegex("Pool: [\\d.,]+")
    private val donUseless by remote.componentRegex("Give Tasty Mithril to Don!")
    private val donRemaining by remote.componentRegex("Remaining: (?:\\d+ Tasty Mithril|FULL)")
    private val donYourMithril by remote.componentRegex("Your Tasty Mithril: \\d+.*")
    private val nearbyPlayers by remote.componentRegex("Nearby Players: .*")
    private val goblinUseless by remote.componentRegex("Kill goblins!")
    private val goblinRemaining by remote.componentRegex("Remaining: \\d+ goblins?")
    private val goblinYourKills by remote.componentRegex("Your kills: \\d+ ☠.*")
    private val mineshaftNotStartedRegex by remote.componentRegex("Not started.*")
    private val mineshaftFortunateFreezingRegex by remote.componentRegex("Event Bonus: \\+\\d+.")
    private val fossilDustRegex by remote.componentRegex("Fossil Dust: [\\d.,]+.*")
    private val compassRegex by remote.componentRegex("Wind Compass")
    private val compassArrowRegex by remote.componentRegex("\\s*[⋖⋗\uE060\uE061≈]+\\s*[⋖⋗\uE060\uE061≈]*\\s*")

    private val patterns = listOf(
        eventRegex, eventZoneRegex, raffleUselessRegex, raffleTicketsRegex, rafflePoolRegex, donUseless,
        donRemaining, donYourMithril, nearbyPlayers, goblinUseless, goblinRemaining, goblinYourKills, mineshaftNotStartedRegex,
        mineshaftFortunateFreezingRegex, fossilDustRegex, compassRegex,
    )


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWith {
            val patterns = powderRegex.takeIf { LinesConfig.showHypixelPowder }?.let { patterns + it } ?: patterns

            event.newComponents.forEach { component ->
                if (patterns.any { it.matches(component) }) {
                    if (LinesConfig.addBetterTogetherTitle && nearbyPlayers.matches(component)) {
                        add(Text.of("Better Together", TextColor.PINK))
                    }
                    add(component)
                }
            }

            event.newComponents.find(compassArrowRegex::matches)?.let {
                add(it align Alignment.CENTER)
            }
        }
    }
}
