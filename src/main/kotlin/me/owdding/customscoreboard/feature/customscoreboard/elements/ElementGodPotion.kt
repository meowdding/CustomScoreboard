package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.Utils.toFormatYears
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI

@ScoreboardElement
object ElementGodPotion : Element() {
    override fun getDisplay(): Any {
        val duration = EffectsAPI.godPotionDuration
        val line = duration.toFormatYears().takeIf { duration.isPositive() } ?: "§cExpired"

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("God Potion", line, "§c")
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT, SkyBlockIsland.THE_CATACOMBS)
    override fun isLineActive() = EffectsAPI.godPotionDuration.isPositive()

    override val configLine: String = "God Potion"
    override val id = "GOD_POTION"
}
