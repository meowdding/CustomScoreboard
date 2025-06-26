package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.time.until

@AutoElement(ElementGroup.MIDDLE)
object ElementCookieBuff : Element() {

    override fun getDisplay(): String {
        if (EffectsAPI.isBoosterCookieActive) {
            val duration = EffectsAPI.boosterCookieExpireTime.until()
            return duration.toComponents { days, hours, minutes, seconds, _ ->
                val text = buildString {
                    if (days > 0) append("${days}d ")
                    if (hours > 0) append("${hours}h ")
                    if (minutes > 0) append("${minutes}m ")
                    if (days <= 0 && seconds > 0) append("${seconds}s") // Only show seconds if there is no days
                    if (isEmpty()) append("0s")
                }.trim()
                val color = if (duration.inWholeSeconds < 60) "§6" else "§f"

                "§dCookie Buff: $color$text"
            }
        } else {
            return "§dCookie Buff: §cExpired"
        }
    }

    override val configLine = "Cookie Buff"
}
