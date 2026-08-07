package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.Utils.toFormatYears
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.asComponent
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object GodPotionElement : Element() {
    override fun getDisplay(): Any {
        val duration = EffectsAPI.godPotionDuration
        val line = duration.toFormatYears().takeIf { duration.isPositive() }?.asComponent() ?: Text.of("Expired", TextColor.RED)

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("God Potion", line, TextColor.RED)
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT, SkyBlockIsland.THE_CATACOMBS)
    override fun isLineActive() = EffectsAPI.godPotionDuration.isPositive()

    override val configLine: String = "God Potion"
    override val id = "GOD_POTION"
}
