package gay.j10a1n15.customscoreboard.feature.customscoreboard.events

import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

object EventMagmaBoss : Event() {
    override fun getDisplay() = formattedLines

    override fun showWhen() = SkyBlockArea.inAnyArea(SkyBlockAreas.MAGMA_CHAMBER)

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.CRIMSON_ISLE)

    override val configLine = "Magma Boss"


    private var formattedLines = mutableListOf<Component>()

    private val bossRegex = ComponentRegex("Boss: \\d+%")
    private val damageSoakedRegex = ComponentRegex("Damage Soaked:")
    private val killMagmasRegex = ComponentRegex("Kill the Magmas:")
    private val formingRegex = ComponentRegex("The boss is (?:re)?forming!")
    private val healthRegex = ComponentRegex("Boss Health:")
    private val healthBarRegex = ComponentRegex("(?:\\d+(?:\\.\\d)?M|\\d+k)/10M❤")
    private val barRegex = ComponentRegex("▎+")

    private val patterns = listOf(bossRegex, damageSoakedRegex, killMagmasRegex, formingRegex, healthRegex, healthBarRegex, barRegex)


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
