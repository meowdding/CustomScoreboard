package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.hotf.HotfAPI
import tech.thatgravyboat.skyblockapi.api.profile.hotm.PowderAPI

@AutoElement
object ElementPowder : Element() {
    private val foragingIsland = setOf(SkyBlockIsland.GALATEA)
    private val miningIslands = setOf(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.CRYSTAL_HOLLOWS, SkyBlockIsland.MINESHAFT)

    override fun getDisplay() = buildList {
        fun format(text: String, powder: String, color: String) = CustomScoreboardRenderer.formatNumberDisplayDisplay(text, powder, color)

        add("§9Powder")
        if (SkyBlockIsland.inAnyIsland(foragingIsland)) {
            when (LinesConfig.powderDisplay) {
                PowderDisplay.CURRENT -> {
                    add(" §7- ${format("Whispers", HotfAPI.whispers.format(), "§b")}")
                }

                PowderDisplay.TOTAL -> {
                    add(" §7- ${format("Whispers", HotfAPI.whispersTotal.format(), "§b")}")
                }

                PowderDisplay.BOTH -> {
                    add(" §7- ${format("Whispers", "${HotfAPI.whispers.format()}/${HotfAPI.whispersTotal.format()}", "§b")}")
                }
            }
        } else if (SkyBlockIsland.inAnyIsland(miningIslands)) {
            when (LinesConfig.powderDisplay) {
                PowderDisplay.CURRENT -> {
                    add(" §7- ${format("Mithril", PowderAPI.mithril.format(), "§2")}")
                    add(" §7- ${format("Gemstone", PowderAPI.gemstone.format(), "§d")}")
                    add(" §7- ${format("Glacite", PowderAPI.glacite.format(), "§b")}")
                }

                PowderDisplay.TOTAL -> {
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
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(*foragingIsland.toTypedArray(), *miningIslands.toTypedArray())

    override val configLine = "Powder / Whispers"

    enum class PowderDisplay(val display: String) {
        CURRENT("Current"),
        TOTAL("Total"),
        BOTH("Current/Total");

        override fun toString() = display
    }
}
