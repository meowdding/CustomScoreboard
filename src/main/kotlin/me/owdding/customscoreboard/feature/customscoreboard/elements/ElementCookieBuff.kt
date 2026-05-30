package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.Utils.toFormatYears
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.until
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.asComponent
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ElementCookieBuff : Element() {

    override fun getDisplay(): Any {
        val line = EffectsAPI.boosterCookieExpireTime.until().toFormatYears().takeIf { EffectsAPI.isBoosterCookieActive }?.asComponent()
            ?: Text.of("Expired", TextColor.RED)

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Cookie Buff", line, TextColor.PINK).withActions {
            hover = listOf("§7Click to open Booster Cookie Menu")
            command = "/boostercookiemenu"
        }
    }

    override fun isLineActive() = EffectsAPI.isBoosterCookieActive

    override val configLine = "Cookie Buff"
    override val id = "COOKIE_BUFF"
}
