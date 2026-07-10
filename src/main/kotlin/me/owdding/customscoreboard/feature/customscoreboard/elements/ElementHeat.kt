package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.area.mining.HollowsAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.helpers.McPlayer
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ElementHeat : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay(
        "Heat",
        if (HollowsAPI.immuneToHeat) Text.of("IMMUNE", TextColor.GOLD)
        else Text.of("${HollowsAPI.heat}♨", TextColor.RED),
        TextColor.RED,
    )

    override fun showWhen() = HollowsAPI.heat != 0 || McPlayer.self?.y?.let { it <= 64 } == true

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.CRYSTAL_HOLLOWS)

    override val configLine = "Heat"
    override val id = "HEAT"
}
