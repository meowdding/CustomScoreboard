package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.formatLong
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.profile.maxwell.MaxwellAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ElementMaxwellPower : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay(
        "Power",
        Text.of(MaxwellAPI.power.name) {
            if (LinesConfig.magicalPower) {
                append(" (", TextColor.GRAY)
                append(MaxwellAPI.accessoryPower.formatLong(), TextColor.GOLD)
                append(")", TextColor.GRAY)
            }
        },
        TextColor.GREEN,
    )

    override val configLine = "Maxwell Power"
    override val id = "MAXWELL_POWER"
}
