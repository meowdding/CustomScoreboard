package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.hub.SpookyFestivalAPI
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@AutoElement
object EventSpooky : Event() {
    override fun getDisplay() = formattedLines

    override val configLine = "Spooky"


    private var formattedLines: List<Component> = emptyList()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (SpookyFestivalAPI.onGoing) {
            formattedLines = listOf(
                Text.of("Spooky Festival ") {
                    color = TextColor.ORANGE
                    append(getTime(), TextColor.WHITE)
                },
                Text.of("Your Candy:", TextColor.GRAY),
                getCandy(),
            )
        } else formattedLines = emptyList()
    }

    private fun getTime(): String {
        val totalSeconds = SpookyFestivalAPI.duration.inWholeSeconds
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    private fun getCandy() = Text.of {
        color = TextColor.GRAY
        append("${SpookyFestivalAPI.greenCandy.format()} Green", TextColor.GREEN)
        append(", ")
        append("${SpookyFestivalAPI.purpleCandy.format()} Purple", TextColor.DARK_PURPLE)
        append(" (")
        append(SpookyFestivalAPI.points.format(), TextColor.ORANGE)
        append(" pts.)")
    }
}
