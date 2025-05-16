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
object EventSpooky : Event() {
    override fun getDisplay() = formattedLine

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND, SkyBlockIsland.GARDEN)

    override val configLine = "Spooky"


    private var formattedLine: Component? = null

    private val festivalRegex = ComponentRegex("Spooky Festival [\\d.,:]+")

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (festivalRegex.regex().anyMatch(event.removed)) {
            formattedLine = null
        }

        festivalRegex.anyMatch(event.addedComponents) {
            formattedLine = it.component
        }

    }
}
