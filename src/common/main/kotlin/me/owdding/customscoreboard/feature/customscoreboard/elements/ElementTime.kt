package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.config.categories.LinesConfig
import tech.thatgravyboat.skyblockapi.api.datetime.DateTimeAPI
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockInstant
import tech.thatgravyboat.skyblockapi.helpers.McLevel
import kotlin.time.Duration.Companion.seconds

@AutoElement(ElementGroup.HEADER)
object ElementTime : Element() {
    override fun getDisplay() = buildString {
        append("§7")

        val hour: Int
        val minutes: Int

        val now = SkyBlockInstant.now()
        if (LinesConfig.smoothTime && isInstantAccurate(now)) {
            hour = now.hour
            minutes = now.minute
        } else {
            hour = DateTimeAPI.hour
            minutes = DateTimeAPI.minute
        }

        val hour12 = if (hour % 12 == 0) 12 else hour % 12
        val period = if (hour >= 12) "pm" else "am"

        append(String.format("%02d:%02d%s", hour12, minutes, period))

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
