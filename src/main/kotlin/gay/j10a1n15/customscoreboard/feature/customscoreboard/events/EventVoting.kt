package gay.j10a1n15.customscoreboard.feature.customscoreboard.events

import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockAreas
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@Module
object EventVoting : Event() {
    override fun getDisplay() = formattedLines

    override fun showWhen() = SkyBlockAreas.ELECTION_ROOM.inArea()

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.HUB)

    override val configLine = "Voting"

    override val configLineHover = listOf(
        "Shows the current voting event.",
        "Only visible in the election room.",
    )


    private val titleRegex = "Year \\d+ Votes".toRegex()
    private val subtitleRegex = "Waiting for|your vote\\.\\.\\.".toRegex()
    private val candidatesRegex = "\\|{15}.+".toRegex()

    private val formattedLines = mutableListOf<Component>()

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        for (component in event.components) {
            if (titleRegex.matches(component.stripped)) {
                formattedLines.add(component)
            } else if (subtitleRegex.matches(component.stripped)) {
                formattedLines.add(component)
            } else if (candidatesRegex.matches(component.stripped)) {
                formattedLines.add(component)
            }
        }
    }
}
