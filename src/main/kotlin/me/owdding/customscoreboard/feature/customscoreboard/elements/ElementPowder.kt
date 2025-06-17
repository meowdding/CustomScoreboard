package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.hotm.PowderAPI

@AutoElement
object ElementPowder : Element() {
    override fun getDisplay() = buildList {
        fun format(text: String, powder: String, color: String) = CustomScoreboardRenderer.formatNumberDisplayDisplay(text, powder, color)

        add("§9Powder")
        when (LinesConfig.powderDisplay) {
            PowderDisplay.CURRENT -> {
                add(" §7- ${format("Mithril", PowderAPI.mithril.format(), "§2")}")
                add(" §7- ${format("Gemstone", PowderAPI.gemstone.format(), "§d")}")
                add(" §7- ${format("Glacite", PowderAPI.glacite.format(), "§b")}")
            }

            PowderDisplay.MAX -> {
                add(" §7- ${format("Mithril", PowderAPI.mithrilTotal.format(), "§2")}")
                add(" §7- ${format("Gemstone", PowderAPI.gemstoneTotal.format(), "§d")}")
                add(" §7- ${format("Glacite", PowderAPI.glaciteTotal.format(), "§b")}")
            }

            PowderDisplay.BOTH -> {
                add(" §7- ${format("Mithril", "${PowderAPI.mithril.format()}/${PowderAPI.mithrilTotal.format()}", "§2")}")
                add(" §7- ${format("Gemstone", "${PowderAPI.gemstone.format()}/${PowderAPI.gemstoneTotal.format()}", "§d")}")
                add(" §7- ${format("Glacite", "${PowderAPI.glacite.format()}/${PowderAPI.glaciteTotal.format()}", "§b")}")
            }
        }
    }

    override fun showIsland() =
        SkyBlockIsland.inAnyIsland(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.CRYSTAL_HOLLOWS, SkyBlockIsland.MINESHAFT)

    override val configLine = "Powder"

    enum class PowderDisplay(val display: String) {
        CURRENT("Current"),
        MAX("Max"),
        BOTH("Current/Max");

        override fun toString() = display
    }
}
