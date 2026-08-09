package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.TextUtils.toComponent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.hotf.WhispersAPI
import tech.thatgravyboat.skyblockapi.api.profile.hotm.PowderAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object PowderElement : Element() {

    private class Currency(
        val name: String,
        val color: Int,
        val current: () -> Long,
        val total: () -> Long,
    )

    private class Region(
        val islands: Set<SkyBlockIsland>,
        val title: String,
        val hoverText: String,
        val command: String,
        val currencies: List<Currency>,
    )

    private val regions = listOf(
        Region(
            islands = setOf(SkyBlockIsland.GALATEA, SkyBlockIsland.TORRHUS_CANYON),
            title = "Whispers",
            hoverText = "§7Click to open your Hotf.",
            command = "/hotf",
            currencies = listOf(
                Currency("Forest", TextColor.DARK_AQUA, { WhispersAPI.forest }, { WhispersAPI.forestTotal }),
                Currency("Desert", TextColor.GOLD, { WhispersAPI.desert }, { WhispersAPI.desertTotal }),
            ),
        ),
        Region(
            islands = setOf(SkyBlockIsland.DWARVEN_MINES, SkyBlockIsland.CRYSTAL_HOLLOWS, SkyBlockIsland.MINESHAFT),
            title = "Powder",
            hoverText = "§7Click to open your Hotm.",
            command = "/hotm",
            currencies = listOf(
                Currency("Mithril", TextColor.DARK_GREEN, { PowderAPI.mithril }, { PowderAPI.mithrilTotal }),
                Currency("Gemstone", TextColor.PINK, { PowderAPI.gemstone }, { PowderAPI.gemstoneTotal }),
                Currency("Glacite", TextColor.AQUA, { PowderAPI.glacite }, { PowderAPI.glaciteTotal }),
            ),
        ),
    )

    private val allIslands = regions.flatMap { it.islands }.toSet()

    private val activeRegion: Region? get() = regions.firstOrNull { SkyBlockIsland.inAnyIsland(it.islands) }

    override fun getDisplay() = buildList {
        val region = activeRegion ?: return@buildList

        add(Text.of(region.title, TextColor.BLUE)) {
            hover = listOf(region.hoverText)
            command = region.command
        }

        region.currencies.forEach { currency ->
            addLine(currency.name, currency.current(), currency.total(), currency.color)
        }
    }

    private fun MutableList<Any>.addLine(name: String, current: Long, total: Long, color: Int) {
        if (LinesConfig.showActiveOnly && !isCurrencyActive(current, total)) return

        val value = when (LinesConfig.powderDisplay) {
            PowderDisplay.CURRENT -> current.format().toComponent()
            PowderDisplay.TOTAL -> total.format().toComponent()
            PowderDisplay.BOTH -> Text.of {
                append(current.format())
                append("/", TextColor.GRAY)
                append(total.format())
            }
        }
        add(
            Text.of {
                append(" - ", TextColor.GRAY)
                append(CustomScoreboardRenderer.formatNumberDisplayDisplay(name.toComponent(), value, color))
            },
        )
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(allIslands)

    override fun isLineActive(): Boolean = activeRegion?.currencies?.any { isCurrencyActive(it.current(), it.total()) } ?: false

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
