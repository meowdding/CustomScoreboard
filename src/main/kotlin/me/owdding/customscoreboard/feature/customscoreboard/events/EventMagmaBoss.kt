package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventMagmaBoss : Event() {
    override fun getDisplay() = formattedLines

    override fun showWhen() = SkyBlockArea.inAnyArea(SkyBlockAreas.MAGMA_CHAMBER)

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.CRIMSON_ISLE)

    override val configLine = "Magma Boss"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val bossRegex by remote.componentRegex("Boss: \\d+%")
    private val damageSoakedRegex by remote.componentRegex("Damage Soaked:")
    private val killMagmasRegex by remote.componentRegex("Kill the Magmas:")
    private val formingRegex by remote.componentRegex("The boss is (?:re)?forming!")
    private val healthRegex by remote.componentRegex("Boss Health:")
    private val healthBarRegex by remote.componentRegex("(?:\\d+(?:\\.\\d)?M|\\d+k)/10M❤")
    private val barRegex by remote.componentRegex("▎+")

    private val patterns = listOf(bossRegex, damageSoakedRegex, killMagmasRegex, formingRegex, healthRegex, healthBarRegex, barRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }
}
