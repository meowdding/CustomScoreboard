package me.owdding.customscoreboard.utils

import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockSeason
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import tech.thatgravyboat.skyblockapi.utils.time.until
import kotlin.time.Duration.Companion.seconds

object Utils {
    fun hasCookieActive() = EffectsAPI.boosterCookieExpireTime.until() > 1.seconds

    private val seasonColors = mapOf(
        SkyBlockSeason.EARLY_SPRING to "§a",
        SkyBlockSeason.SPRING to "§a",
        SkyBlockSeason.LATE_SPRING to "§a",
        SkyBlockSeason.EARLY_SUMMER to "§e",
        SkyBlockSeason.SUMMER to "§e",
        SkyBlockSeason.LATE_SUMMER to "§e",
        SkyBlockSeason.EARLY_AUTUMN to "§6",
        SkyBlockSeason.AUTUMN to "§6",
        SkyBlockSeason.LATE_AUTUMN to "§6",
        SkyBlockSeason.EARLY_WINTER to "§9",
        SkyBlockSeason.WINTER to "§9",
        SkyBlockSeason.LATE_WINTER to "§9",
    )

    fun SkyBlockSeason.getColoredName(): String = seasonColors[this] + this.toString()

    fun <T> Collection<T>.nextAfter(element: T, skip: Int = 1): T? {
        val index = indexOfFirst { if (it is Component && element is String) it.stripped == element else it == element }
        if (index == -1 || index + skip >= size) return null
        return elementAt(index + skip)
    }
}
