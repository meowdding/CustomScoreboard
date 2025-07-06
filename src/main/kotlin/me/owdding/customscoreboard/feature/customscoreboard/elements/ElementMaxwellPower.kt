package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import tech.thatgravyboat.skyblockapi.api.profile.maxwell.MaxwellAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString

@AutoElement(ElementGroup.MIDDLE)
object ElementMaxwellPower : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay(
        "Power",
        MaxwellAPI.power.name + if (LinesConfig.magicalPower) " §7(§6${MaxwellAPI.magicalPower.toFormattedString()}§7)" else "",
        "§a",
    )

    override val configLine = "Maxwell Power"
}
