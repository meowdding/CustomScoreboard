package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ColdElement : Element() {
    override fun getDisplay() = CustomScoreboardRenderer.formatNumberDisplayDisplay("Cold", "${-GlaciteAPI.cold}❄", "§b").withActions {
        hover(Text.of("Click to warp to the basecamp.", TextColor.GRAY))
        command = "/warp basecamp"
    }

    override fun showWhen() = GlaciteAPI.inColdArea() && (GlaciteAPI.cold > 0)

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.SAFARI, SkyBlockIsland.MINESHAFT)

    override val configLine = "Cold"
    override val id = "COLD"
}
