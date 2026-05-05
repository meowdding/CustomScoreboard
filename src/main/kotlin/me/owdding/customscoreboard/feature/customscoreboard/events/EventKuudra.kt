package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventKuudra : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.KUUDRA)

    override val configLine = "Kuudra"
    override val configLineHover = listOf("These have not been tested as I don't play Kuudra.", "Please report any issues.")


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val autoCloseRegex by remote.componentRegex("Auto-closing in: [\\d+:,.]+")
    private val startingInRegex by remote.componentRegex("Starting in: [\\d+:,.]+")
    private val timeElapsedRegex by remote.componentRegex("Time Elapsed: (?<time>(\\w+[ydhms] ?)+)")
    private val instanceShutdownRegex by remote.componentRegex("Instance Shutdown In: (?<time>(?:\\w+[ydhms] ?)+)")
    private val waveRegex by remote.componentRegex("Wave: \\d+(?: - \\d+:\\d+)?")
    private val tokensRegex by remote.componentRegex("Tokens: [\\w,]+")
    private val submergesRegex by remote.componentRegex("Submerges In: [\\w\\s?]+")

    private val patterns =
        listOf(autoCloseRegex, startingInRegex, timeElapsedRegex, instanceShutdownRegex, waveRegex, tokensRegex, submergesRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }
}
