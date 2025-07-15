package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import tech.thatgravyboat.skyblockapi.api.area.mining.HollowsAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.helpers.McPlayer

@AutoElement
object ElementHeat : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay(
        "Heat",
        if (HollowsAPI.immuneToHeat) "§6IMMUNE"
        else "§c${HollowsAPI.heat}♨",
        "§c",
    )

    override fun showWhen() = HollowsAPI.heat != 0 || McPlayer.self?.y?.let { it <= 64 } == true

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.CRYSTAL_HOLLOWS)

    override val configLine = "Heat"
}
