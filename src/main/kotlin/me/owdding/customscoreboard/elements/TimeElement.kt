package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.datetime.DateTimeAPI
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockInstant
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color
import kotlin.time.Duration.Companion.seconds

@ScoreboardElement
object TimeElement : Element() {
    override fun getDisplay() = Text.of {
        color = TextColor.GRAY

        var rawHour: Int
        val minutes: Int

        val now = SkyBlockInstant.now()
        if (LinesConfig.smoothTime && isInstantAccurate(now)) {
            rawHour = now.hour
            minutes = now.minute
        } else {
            rawHour = DateTimeAPI.hour
            minutes = DateTimeAPI.minute
        }

        val displayHour = if (!LinesConfig.time24hFormat) if (rawHour % 12 == 0) 12 else rawHour % 12
        else rawHour


        append(String.format("%02d:%02d", displayHour, minutes))

        if (!LinesConfig.time24hFormat) {
            if (rawHour >= 12) append("pm") else append("am")
        }

        currentWeather?.let {
            append(" ")
            append(it)
        }
    }

    override val configLine = "Time"
    override val id = "TIME"
    override val group = ElementGroup.HEADER

    private val remote = RemoteStrings.resolve()
    private val weatherRegex by remote.componentRegex("\\s*\\d{1,2}:\\d{2}(?:am|pm) (?<weather>.+)")

    private var currentWeather: Component? = null

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        val match = weatherRegex.anyMatch(event.newComponents, "weather") { (weather) ->
            currentWeather = weather
        }
        if (!match) currentWeather = null
    }

    private fun isInstantAccurate(instant: SkyBlockInstant): Boolean {
        val season = DateTimeAPI.season ?: return false

        val realInstant = SkyBlockInstant(
            instant.year,
            season.ordinal + 1,
            DateTimeAPI.day,
            DateTimeAPI.hour,
            DateTimeAPI.minute + instant.minute % 10,
        )

        val diff = (realInstant - instant).absoluteValue
        return diff < 15.seconds
    }
}
