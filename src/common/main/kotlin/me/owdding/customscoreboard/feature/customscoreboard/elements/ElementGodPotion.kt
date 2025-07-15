package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.Utils.toFormatYears
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI

@AutoElement(ElementGroup.MIDDLE)
object ElementGodPotion : Element() {
    override fun getDisplay(): Any? {
        val duration = EffectsAPI.godPotionDuration
        val line = duration.toFormatYears().takeIf { duration.isPositive() } ?: "§cExpired"

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("God Potion", line, "§c")
    }

    override val configLine: String = "God Potion"
}
