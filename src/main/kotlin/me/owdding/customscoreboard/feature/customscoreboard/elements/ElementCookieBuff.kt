package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.time.until

@AutoElement(ElementGroup.MIDDLE)
object ElementCookieBuff : Element() {

    override fun getDisplay(): Any {
        return if (EffectsAPI.isBoosterCookieActive) {
            val duration = EffectsAPI.boosterCookieExpireTime.until()

            val years = duration.inWholeDays / 365
            val days = duration.inWholeDays % 365
            val hours = duration.inWholeHours % 24
            val minutes = duration.inWholeMinutes % 60
            val seconds = duration.inWholeSeconds % 60

            CustomScoreboardRenderer.formatNumberDisplayDisplay(
                "Cookie Buff",
                buildString {
                    if (years > 0) append("${years}y ")
                    if (days > 0) append("${days}d ")
                    if (hours > 0) append("${hours}h ")
                    if (minutes > 0) append("${minutes}m ")
                    if (years <= 0 && days <= 0 && seconds > 0) append("${seconds}s") // Only show seconds if there is no days or years
                    if (isEmpty()) append("0s")
                },
                "§d"
            )
        } else {
            "§dCookie Buff: §cExpired"
        }.withActions {
            hover = listOf("§7Click to open Booster Cookie Menu")
            command = "/boostercookiemenu"
        }
    }

    override val configLine = "Cookie Buff"
}
