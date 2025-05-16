package gay.j10a1n15.customscoreboard.feature.customscoreboard.events

import gay.j10a1n15.customscoreboard.utils.Utils.nextAfter
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@Module
object EventTrapper : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_BARN)

    override val configLine = "Dark Auction"


    private var formattedLines = mutableListOf<Component>()

    private val peltsRegex = ComponentRegex("Pelts: [\\d,.]+.*")
    private val mobLocationRegex = ComponentRegex("Tracker Mob Location:")


    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        val lines = event.components
        val pelts = lines.find { peltsRegex.matches(it) }
        val location = lines.find { mobLocationRegex.matches(it) }
        val actualLocation = lines.nextAfter(location)

        formattedLines = listOfNotNull(pelts, location, actualLocation).toMutableList()
    }

}
