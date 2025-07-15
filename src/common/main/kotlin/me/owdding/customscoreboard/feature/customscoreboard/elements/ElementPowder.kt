package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.hotf.WhispersAPI
import tech.thatgravyboat.skyblockapi.api.profile.hotm.PowderAPI

@AutoElement
object ElementPowder : Element() {
    private val foragingIsland = setOf(SkyBlockIsland.GALATEA)
    private val miningIslands = setOf(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.CRYSTAL_HOLLOWS, SkyBlockIsland.MINESHAFT)
    private val allIslands = foragingIsland + miningIslands

    override fun getDisplay() = buildList {
        when {
            SkyBlockIsland.inAnyIsland(foragingIsland) -> {
                add("§9Whispers") {
                    hover = listOf("§7Click to open your Hotf.")
                    command = "/hotf"
                }

                addLine("Forest", WhispersAPI.forest, WhispersAPI.forestTotal, "§3")
            }

            SkyBlockIsland.inAnyIsland(miningIslands) -> {
                add("§9Powder") {
                    hover = listOf("§7Click to open your Hotm.")
                    command = "/hotm"
                }

                addLine("Mithril", PowderAPI.mithril, PowderAPI.mithrilTotal, "§2")
                addLine("Gemstone", PowderAPI.gemstone, PowderAPI.gemstoneTotal, "§d")
                addLine("Glacite", PowderAPI.glacite, PowderAPI.glaciteTotal, "§b")
            }
        }
    }

    private fun MutableList<Any>.addLine(name: String, current: Number, total: Number, color: String) {
        val value = when (LinesConfig.powderDisplay) {
            PowderDisplay.CURRENT -> current.format()
            PowderDisplay.TOTAL -> total.format()
            PowderDisplay.BOTH -> "${current.format()}/${total.format()}"
        }

        add(" §7- ${CustomScoreboardRenderer.formatNumberDisplayDisplay(name, value, color)}")
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(allIslands)

    override val configLine = "Powder / Whispers"

    enum class PowderDisplay(val display: String) {
        CURRENT("Current"),
        TOTAL("Total"),
        BOTH("Current/Total");

        override fun toString() = display
    }
}
