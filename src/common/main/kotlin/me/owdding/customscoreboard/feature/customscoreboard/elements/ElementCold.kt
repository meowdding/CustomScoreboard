package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
@ScoreboardElement
object ElementCold : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay("Cold", "${-GlaciteAPI.cold}❄", "§b").withActions {
        hover = listOf("§7Click to warp to the basecamp.")
        command = "/warp basecamp"
    }

    override fun showWhen() = GlaciteAPI.inGlaciteTunnels() && (GlaciteAPI.cold > 0)

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.MINESHAFT)

    override val configLine = "Cold"
    override val id = "COLD"
}
