package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.hotf.WhispersAPI
import tech.thatgravyboat.skyblockapi.api.profile.hotm.PowderAPI

@ScoreboardElement
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

    private fun MutableList<Any>.addLine(name: String, current: Long, total: Long, color: String) {
        val value = when (LinesConfig.powderDisplay) {
            PowderDisplay.CURRENT -> current.format()
            PowderDisplay.TOTAL -> total.format()
            PowderDisplay.BOTH -> "${current.format()}/${total.format()}"
        }
        if (LinesConfig.showActiveOnly && !isCurrencyActive(current, total)) return

        add(" §7- ${CustomScoreboardRenderer.formatNumberDisplayDisplay(name, value, color)}")
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(allIslands)
    override fun isLineActive() = when {
        SkyBlockIsland.inAnyIsland(foragingIsland) -> isCurrencyActive(WhispersAPI.forest, WhispersAPI.forestTotal)
        SkyBlockIsland.inAnyIsland(miningIslands) -> {
            isCurrencyActive(PowderAPI.mithril, PowderAPI.mithrilTotal) ||
                isCurrencyActive(PowderAPI.gemstone, PowderAPI.gemstoneTotal) ||
                isCurrencyActive(PowderAPI.glacite, PowderAPI.glaciteTotal)
        }

        else -> false
    }

    private fun isCurrencyActive(current: Long, total: Long): Boolean = when (LinesConfig.powderDisplay) {
        PowderDisplay.CURRENT -> current > 0
        PowderDisplay.TOTAL -> total > 0
        PowderDisplay.BOTH -> current > 0 || total > 0
    }

    override val configLine = "Powder / Whispers"
    override val id = "POWDER"

    enum class PowderDisplay(val display: String) {
        CURRENT("Current"),
        TOTAL("Total"),
        BOTH("Current/Total");

        override fun toString() = display
    }
}
