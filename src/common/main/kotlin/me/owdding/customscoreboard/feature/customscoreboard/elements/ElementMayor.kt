package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.extensions.toReadableTime
import tech.thatgravyboat.skyblockapi.api.area.hub.ElectionAPI
import tech.thatgravyboat.skyblockapi.api.data.Candidate
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockInstant
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.text.TextUtils.splitToWidth
import kotlin.time.Duration

@AutoElement
@ScoreboardElement
object ElementMayor : Element() {
    override fun getDisplay() = buildList {
        val mayor = ElectionAPI.currentMayor ?: return@buildList
        val time = if (LinesConfig.showMayorTime) " §7(§e${timeUntilNextMayor().toReadableTime()}§7)" else ""

        val perksDisplay = LinesConfig.mayorPerksDisplay
        val count = if (perksDisplay == PerkDisplay.COUNT) " §e(${mayor.activePerks.size})" else ""

        val ministerDisplay = LinesConfig.ministerDisplay
        val minister = ElectionAPI.currentMinister
        val ministerCompact = if (ministerDisplay == MinisterDisplay.COMPACT && minister != null) {
            "§7, ${minister.formatName()}"
        } else ""

        add("${mayor.formatName()}$count$ministerCompact$time") {
            hover = if (perksDisplay != PerkDisplay.ALL) buildList {
                addHoverPerks(mayor)
                if (minister != null && ministerDisplay == MinisterDisplay.COMPACT) {
                    add("")
                    addHoverPerks(minister)
                }
                add("")
                add("")
                add("§eClick to open the calendar.")
            } else listOf("§7Click to open the calendar.")
            command = "/calendar"
        }

        if (perksDisplay == PerkDisplay.ALL) {
            addPerks(mayor)
        }

        if (minister != null && ministerDisplay == MinisterDisplay.FULL) {
            add(minister.formatName()) {
                if (perksDisplay == PerkDisplay.OFF) {
                    hover = buildList { addHoverPerks(minister) }
                }
            }

            if (perksDisplay == PerkDisplay.ALL) {
                addPerks(minister)
            }
        }
    }

    private fun MutableList<Any>.addPerks(candidate: Candidate) {
        val color = candidateColor[candidate] ?: "§e"
        candidate.activePerks.forEach { perk ->
            add(" §7- $color${perk.perkName}") {
                hover = perk.description.splitToWidth(" ", 140).map { "§7$it" }
            }
        }
    }

    private fun MutableList<String>.addHoverPerks(candidate: Candidate) {
        val color = candidateColor[candidate] ?: "§e"
        candidate.activePerks.forEachIndexed { i, perk ->
            if (i != 0) add("")
            add("$color${perk.perkName}:")
            perk.description.splitToWidth(" ", 140).mapTo(this) { "  §7$it" }
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override fun showWhen() = ElectionAPI.currentMayor != null

    override val configLine = "Mayor"
    override val id = "MAYOR"


    private const val ELECTION_MONTH = 3
    private const val ELECTION_DAY = 27

    private fun timeUntilNextMayor(): Duration {
        val instant = SkyBlockInstant.now()

        val mayorYear = if (instant.month < ELECTION_MONTH || (instant.day < ELECTION_DAY && instant.month == ELECTION_MONTH)) {
            instant.year
        } else {
            instant.year + 1
        }

        return SkyBlockInstant(mayorYear, 3, 27) - instant
    }

    private fun Candidate.formatName(): String = (candidateColor[this] ?: "§e") + candidateName

    private val candidateColor = mapOf(
        Candidate.AATROX to "§3",
        Candidate.COLE to "§e",
        Candidate.DIANA to "§2",
        Candidate.DIAZ to "§6",
        Candidate.FINNEGAN to "§c",
        Candidate.FOXY to "§d",
        Candidate.MARINA to "§b",
        Candidate.PAUL to "§c",
        Candidate.SCORPIUS to "§d",
        Candidate.JERRY to "§d",
        Candidate.DERPY to "§d",
    )

    enum class PerkDisplay(private val display: String) {
        OFF("Off"),
        COUNT("Perk Amount"),
        ALL("All Perks"),
        ;

        override fun toString(): String = display
    }

    enum class MinisterDisplay(private val display: String) {
        OFF("Off"),
        COMPACT("Compact"),
        FULL("Show"),
        ;

        override fun toString(): String = display
    }
}
