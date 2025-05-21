package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventWinter : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.JERRYS_WORKSHOP)

    override val configLine = "Winter"


    private var formattedLines = mutableListOf<Component>()

    private val startRegex = ComponentRegex("Event Start: [\\d:,.]+")
    private val nextWaveRegex = ComponentRegex("Next Wave: (?:[\\d:,.]+|Soon!)")
    private val waveRegex = ComponentRegex("Wave \\d+")
    private val magmaLeftRegex = ComponentRegex("Magma Cubes Left: -?\\d+")
    private val totalDamageRegex = ComponentRegex("Your Total Damage: [\\d+,.]+.*")
    private val cubeDamageRegex = ComponentRegex("Your Cube Damage: §.[\\d+,.]+")

    private val patterns = listOf(startRegex, nextWaveRegex, waveRegex, magmaLeftRegex, totalDamageRegex, cubeDamageRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        formattedLines.addAll(
            event.components.filter { component ->
                patterns.any { it.matches(component) }
            },
        )
    }

}
