package gay.j10a1n15.customscoreboard.feature.customscoreboard.events

import gay.j10a1n15.customscoreboard.utils.Utils.nextAfter
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@Module
object EventDarkAuction : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.DARK_AUCTION)

    override val configLine = "Dark Auction"


    private var formattedLines = mutableListOf<Component>()

    private val startingInRegex = ComponentRegex("Starting in: [\\w:,.\\s]+")
    private val timeLeftRegex = ComponentRegex("Time Left: [\\w:,.\\s]+")
    private val currentItemRegex = ComponentRegex("Current Item:")


    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        val lines = event.components
        val startingIn = lines.find { startingInRegex.matches(it) }
        val timeLeft = lines.find { timeLeftRegex.matches(it) }
        val currentItem = lines.find { currentItemRegex.matches(it) }
        val item = lines.nextAfter(currentItem)

        formattedLines = listOfNotNull(startingIn, timeLeft, currentItem, item).toMutableList()
    }

}
