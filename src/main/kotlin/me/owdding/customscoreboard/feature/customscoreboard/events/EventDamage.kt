package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@AutoElement
object EventDamage : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_END)

    override val configLine = "Damage"


    private var formattedLines = mutableListOf<Component>()

    private val hpRegex = ComponentRegex("(?:Protector|Dragon) HP: [\\d,.]* ❤")
    private val damageRegex = ComponentRegex("Your Damage: [\\d,.]+")

    private val patterns = listOf(hpRegex, damageRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        formattedLines.addAll(
            event.components.filter { component ->
                patterns.any { it.matches(component) }
            },
        )
    }
}
