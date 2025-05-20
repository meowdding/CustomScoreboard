package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@Module
@AutoElement
object EventCarnival : Event() {
    override fun getDisplay() = formattedLines

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.HUB)

    override val configLine = "Carnival"


    private var formattedLines = mutableListOf<Component>()

    private val timeRegex = ComponentRegex("Carnival [\\d:,.]+")
    private val tokensRegex = ComponentRegex("Carnival Tokens: [\\d,.]+")
    private val taskRegex = ComponentRegex("Catch a Fish|Fruit Digging|Zombie Shootout")
    private val timeLeftRegex = ComponentRegex("Time Left: [\\w:,.\\s]+")
    private val fruitsRegex = ComponentRegex("Fruits: \\d+/\\d+")
    private val scoreRegex = ComponentRegex("Score: \\d+.*")
    private val catchStreakRegex = ComponentRegex("Catch Streak: \\d+")
    private val accuracyRegex = ComponentRegex("Accuracy: [\\d.,]+%")
    private val killsRegex = ComponentRegex("Kills: \\d+")

    private val patterns =
        listOf(timeRegex, tokensRegex, taskRegex, timeLeftRegex, fruitsRegex, scoreRegex, catchStreakRegex, accuracyRegex, killsRegex)


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
