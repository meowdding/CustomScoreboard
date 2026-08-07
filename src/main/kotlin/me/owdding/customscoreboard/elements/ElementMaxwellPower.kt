package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.profile.maxwell.MaxwellAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString

@ScoreboardElement
object ElementMaxwellPower : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay(
        "Power",
        MaxwellAPI.power.name + if (LinesConfig.magicalPower) " §7(§6${MaxwellAPI.magicalPower.toFormattedString()}§7)" else "",
        "§a",
    )

    override val configLine = "Maxwell Power"
    override val id = "MAXWELL_POWER"
}
