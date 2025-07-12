package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import kotlin.time.Duration.Companion.seconds

@AutoElement(ElementGroup.MIDDLE)
object ElementGodPotion : Element() {
    override fun getDisplay(): Any? {
        val duration = EffectsAPI.godPotionDuration

        val years = duration.inWholeDays / 365
        val days = duration.inWholeDays % 365
        val hours = duration.inWholeHours % 24
        val minutes = duration.inWholeMinutes % 60
        val seconds = duration.inWholeSeconds % 60

        val line = buildString {
            if (years > 0) append("${years}y ")
            if (days > 0) append("${days}d ")
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}m ")
            if (years <= 0 && days <= 0 && seconds > 0) append("${seconds}s") // Only show seconds if there is no days or years
            if (isEmpty()) append("0s")
        }.takeIf { EffectsAPI.godPotionDuration > 0.seconds } ?: "§cExpired"

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("God Potion", line, "§c")
    }

    override val configLine: String = "God Potion"
}
