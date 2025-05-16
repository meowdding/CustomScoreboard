package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.area.mining.PowderAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

object ElementPowder : Element() {
    override fun getDisplay() = buildList {
        add("§9Powder")
        // todo: option for full powder
        add(" §7- ${CustomScoreboardRenderer.formatNumberDisplayDisplay("Mithril", PowderAPI.mithril.format(), "§2")}")
        add(" §7- ${CustomScoreboardRenderer.formatNumberDisplayDisplay("Gemstone", PowderAPI.gemstone.format(), "§d")}")
        add(" §7- ${CustomScoreboardRenderer.formatNumberDisplayDisplay("Glacite", PowderAPI.glacite.format(), "§b")}")
    }

    override fun showIsland() =
        SkyBlockIsland.inAnyIsland(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.CRYSTAL_HOLLOWS, SkyBlockIsland.MINESHAFT)

    override val configLine = "Powder"
}
