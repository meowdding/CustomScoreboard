package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.category.CustomPrefix
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.Utils.toFormatYears
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.extentions.parseColonDuration
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.anyMatch
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import kotlin.time.Duration

@AutoElement
object FlightDurationEvent : Event() {
    override fun getDisplay() = duration?.let {
        val formatted = when (LinesConfig.flightDurationFormat) {
            FlightDurationFormat.COLON -> {
                val hours = it.inWholeHours
                val minutes = (it.inWholeMinutes % 60).toString().padStart(2, '0')
                val seconds = (it.inWholeSeconds % 60).toString().padStart(2, '0')
                if (hours > 0) "$hours:$minutes:$seconds" else "$minutes:$seconds"
            }

            FlightDurationFormat.LETTERS -> it.toFormatYears()
        }

        CustomScoreboardRenderer.formatNumberDisplayDisplay(CustomPrefix.FLIGHT_DURATION, formatted, TextColor.GREEN)
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.PRIVATE_ISLAND, SkyBlockIsland.GARDEN)

    override val configLine = "Flight Duration"


    private var duration: Duration? = null

    private val flightRegex by RemoteStrings.resolve().regex("Flight Duration: (?<time>[\\d:]+)")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        val found = flightRegex.anyMatch(event.new, "time") { (time) ->
            duration = time.parseColonDuration()
        }
        if (!found) duration = null
    }

    enum class FlightDurationFormat(val display: String) {
        COLON("340:46:14"),
        LETTERS("14d 4h"),
        ;

        override fun toString(): String = display
    }
}
