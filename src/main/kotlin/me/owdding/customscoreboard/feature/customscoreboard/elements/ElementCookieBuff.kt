package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.time.until

@AutoElement(ElementGroup.MIDDLE)
object ElementCookieBuff : Element() {

    override fun getDisplay(): String {
        return EffectsAPI.boosterCookieExpireTime.until().toComponents { days, hours, minutes, seconds, _ -> when {
            days > 0 -> "§dCookie Buff: §f${days}d ${hours}h ${minutes}m"
            hours > 0 -> "§dCookie Buff: §f${hours}h ${minutes}m ${seconds}s"
            minutes > 0 -> "§dCookie Buff: §f${minutes}m ${seconds}s"
            seconds > 0 -> "§dCookie Buff: §6${seconds}s"
            else -> "§dCookie Buff: §cExpired"
        } }
    }

    override val configLine = "Cookie Buff"
}
