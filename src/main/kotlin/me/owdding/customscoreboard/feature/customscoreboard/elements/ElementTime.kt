package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.datetime.DateTimeAPI
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockInstant
import tech.thatgravyboat.skyblockapi.helpers.McLevel
import kotlin.time.Duration.Companion.seconds

@ScoreboardElement
object ElementTime : Element() {
    override fun getDisplay() = buildString {
        append("§7")

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

        val symbol = if (McLevel.hasLevel) {
            when {
                McLevel.self.isRaining -> "§3☔"
                McLevel.self.isThundering -> "§e⚡"
                DateTimeAPI.isDay -> "§e☀"
                else -> "§b☽"
            }
        } else {
            "§c⚠"
        }

        append(" $symbol")
    }

    override val configLine = "Time"
    override val id = "TIME"
    override val group = ElementGroup.HEADER

    private fun isInstantAccurate(instant: SkyBlockInstant): Boolean {
        val season = DateTimeAPI.season ?: return false

        val realInstant = SkyBlockInstant(
            instant.year,
            season.ordinal + 1,
            DateTimeAPI.day,
            DateTimeAPI.hour,
            DateTimeAPI.minute + instant.minute%10,
        )

        val diff = (realInstant - instant).absoluteValue
        return diff < 15.seconds
    }
}
