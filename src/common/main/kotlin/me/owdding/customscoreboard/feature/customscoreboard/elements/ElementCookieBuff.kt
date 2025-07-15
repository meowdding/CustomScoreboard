package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.Utils.toFormatYears
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.time.until

@AutoElement(ElementGroup.MIDDLE)
object ElementCookieBuff : Element() {

    override fun getDisplay(): Any {
        val line = EffectsAPI.boosterCookieExpireTime.until().toFormatYears().takeIf { EffectsAPI.isBoosterCookieActive } ?: "§cExpired"

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Cookie Buff", line, "§d").withActions {
            hover = listOf("§7Click to open Booster Cookie Menu")
            command = "/boostercookiemenu"
        }
    }

    override val configLine = "Cookie Buff"
}
