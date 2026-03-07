package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.replaceWithMatches
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object EventCarnival : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.HUB)

    override val configLine = "Carnival"


    private val formattedLines = mutableListOf<Component>()

    private val remote = RemoteStrings.resolve()
    private val timeRegex by remote.componentRegex("Carnival [\\d:,.]+")
    private val tokensRegex by remote.componentRegex("Carnival Tokens: [\\d,.]+")
    private val taskRegex by remote.componentRegex("Catch a Fish|Fruit Digging|Zombie Shootout")
    private val timeLeftRegex by remote.componentRegex("Time Left: [\\w:,.\\s]+")
    private val fruitsRegex by remote.componentRegex("Fruits: \\d+/\\d+")
    private val scoreRegex by remote.componentRegex("Score: \\d+.*")
    private val catchStreakRegex by remote.componentRegex("Catch Streak: \\d+")
    private val accuracyRegex by remote.componentRegex("Accuracy: [\\d.,]+%")
    private val killsRegex by remote.componentRegex("Kills: \\d+")

    private val patterns =
        listOf(timeRegex, tokensRegex, taskRegex, timeLeftRegex, fruitsRegex, scoreRegex, catchStreakRegex, accuracyRegex, killsRegex)


    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.replaceWithMatches(event.components, patterns)
    }
}
