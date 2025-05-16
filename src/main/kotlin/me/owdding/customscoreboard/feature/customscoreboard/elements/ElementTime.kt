package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.datetime.DateTimeAPI
import tech.thatgravyboat.skyblockapi.helpers.McLevel

@Module
object ElementTime : Element() {
    override fun getDisplay() = buildString {
        append("§7")

        val hour = DateTimeAPI.hour
        val hour12 = if (hour % 12 == 0) 12 else hour % 12
        val period = if (hour >= 12) "pm" else "am"

        append(String.format("%02d:%02d%s", hour12, DateTimeAPI.minute, period))

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
}
