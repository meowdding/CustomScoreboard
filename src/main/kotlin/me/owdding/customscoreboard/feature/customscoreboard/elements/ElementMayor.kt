package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.extensions.toReadableTime
import tech.thatgravyboat.skyblockapi.api.area.hub.ElectionAPI
import tech.thatgravyboat.skyblockapi.api.data.MayorCandidate
import tech.thatgravyboat.skyblockapi.api.data.MayorCandidates
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockInstant
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.extentions.until
import tech.thatgravyboat.skyblockapi.utils.text.TextUtils.splitToWidth
import kotlin.time.Duration

@ScoreboardElement
object ElementMayor : Element() {
    override fun getDisplay() = buildList {
        val jerryActive = MayorCandidates.JERRY.isActive && LinesConfig.showJerryInMinister && ElectionAPI.currentJerryCandidate != null
        val mayor = ElectionAPI.mayor ?: return@buildList

        val perksDisplay = LinesConfig.mayorPerksDisplay
        val count = if (perksDisplay == PerkDisplay.COUNT) " §e(${mayor.activePerks.size})" else ""

        val ministerDisplay = LinesConfig.ministerDisplay
        val minister = ElectionAPI.minister ?: if (jerryActive) ElectionAPI.currentJerryCandidate?.first else null
        val ministerCompact = if (ministerDisplay == MinisterDisplay.COMPACT && minister != null) {
            "§7, ${minister.formatName()}"
        } else ""

        val time = if (LinesConfig.showMayorTime) buildString {
            append(" §7(§e${timeUntilNextMayor().toReadableTime()}")
            if (jerryActive && ministerDisplay == MinisterDisplay.COMPACT) append("§7, §e${timeUntilJerryMayor()?.toReadableTime()}")
            append("§7)")
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
            val name = buildString {
                append(minister.formatName())
                if (jerryActive) append(" §7(§e${timeUntilJerryMayor()?.toReadableTime()}§7)")
            }
            add(name) {
                if (perksDisplay == PerkDisplay.OFF) {
                    hover = buildList { addHoverPerks(minister) }
                }
            }

            if (perksDisplay == PerkDisplay.ALL) {
                addPerks(minister)
            }
        }
    }

    private fun MutableList<Any>.addPerks(candidate: MayorCandidate) {
        val color = candidateColor[candidate] ?: "§e"
        candidate.activePerks.forEach { perk ->
            add(" §7- $color${perk.perkName}") {
                hover = perk.description.splitToWidth(" ", 140).map { "§7$it" }
            }
        }
    }

    private fun MutableList<String>.addHoverPerks(candidate: MayorCandidate) {
        val color = candidateColor[candidate] ?: "§e"
        candidate.activePerks.forEachIndexed { i, perk ->
            if (i != 0) add("")
            add("$color${perk.perkName}:")
            perk.description.splitToWidth(" ", 140).mapTo(this) { "  §7$it" }
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override fun showWhen() = ElectionAPI.mayor != null

    override val configLine = "Mayor"
    override val id = "MAYOR"


    private const val ELECTION_MONTH = 3
    private const val ELECTION_DAY = 27

    private fun timeUntilJerryMayor(): Duration? = ElectionAPI.currentJerryCandidate?.second?.until()

    private fun timeUntilNextMayor(): Duration {
        val instant = SkyBlockInstant.now()

        val mayorYear = if (instant.month < ELECTION_MONTH || (instant.day < ELECTION_DAY && instant.month == ELECTION_MONTH)) {
            instant.year
        } else {
            instant.year + 1
        }

        return SkyBlockInstant(mayorYear, 3, 27) - instant
    }

    private fun MayorCandidate.formatName(): String = (candidateColor[this] ?: "§e") + candidateName

    private val candidateColor = mapOf(
        MayorCandidates.AATROX to "§3",
        MayorCandidates.COLE to "§e",
        MayorCandidates.DIANA to "§2",
        MayorCandidates.DIAZ to "§6",
        MayorCandidates.FINNEGAN to "§c",
        MayorCandidates.FOXY to "§d",
        MayorCandidates.MARINA to "§b",
        MayorCandidates.PAUL to "§c",
        MayorCandidates.SCORPIUS to "§d",
        MayorCandidates.JERRY to "§d",
        MayorCandidates.DERPY to "§d",
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
