package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.profile.maxwell.MaxwellAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString
import java.text.NumberFormat
import java.util.Locale

@ScoreboardElement
object ElementMaxwellPower : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay(
        "Power",
        MaxwellAPI.power.name + if (LinesConfig.magicalPower) " §7(§6${NumberFormat.getNumberInstance(Locale.US).format(MaxwellAPI.magicalPower)}§7)" else "",
        "§a",
    )

    override val configLine = "Maxwell Power"
    override val id = "MAXWELL_POWER"
}
