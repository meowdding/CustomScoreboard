package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventWinter : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.JERRYS_WORKSHOP)

    override val configLine = "Winter"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val startRegex by remote.componentRegex("Event Start: [\\d:,.]+")
    private val nextWaveRegex by remote.componentRegex("Next Wave: (?:[\\d:,.]+|Soon!)")
    private val waveRegex by remote.componentRegex("Wave \\d+")
    private val magmaLeftRegex by remote.componentRegex("Magma Cubes Left: -?\\d+")
    private val totalDamageRegex by remote.componentRegex("Your Total Damage: [\\d+,.]+.*")
    private val cubeDamageRegex by remote.componentRegex("Your Cube Damage: §.[\\d+,.]+")

    private val patterns = listOf(startRegex, nextWaveRegex, waveRegex, magmaLeftRegex, totalDamageRegex, cubeDamageRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }

}
