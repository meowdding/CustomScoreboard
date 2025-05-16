package gay.j10a1n15.customscoreboard.feature.customscoreboard.events

import gay.j10a1n15.customscoreboard.utils.TextUtils.anyMatch
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@Module
object EventRedstone : Event() {
    override fun getDisplay() = formattedLine

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND)

    override val configLine = "Redstone"


    private var formattedLine: Component? = null

    private val redstoneRegex = ComponentRegex(" ⚡ Redstone: [\\d.,]+%")

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (redstoneRegex.regex().anyMatch(event.removed)) {
            formattedLine = null
        }

        redstoneRegex.anyMatch(event.addedComponents) {
            formattedLine = it.component
        }

    }
}
