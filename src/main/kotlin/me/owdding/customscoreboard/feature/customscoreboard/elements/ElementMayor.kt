package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.lib.extensions.toReadableTime
import tech.thatgravyboat.skyblockapi.api.area.hub.ElectionAPI
import tech.thatgravyboat.skyblockapi.api.data.Candidate
import tech.thatgravyboat.skyblockapi.api.data.Perk
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockInstant
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.text.TextUtils.splitToWidth
import kotlin.time.Duration

@AutoElement
object ElementMayor : Element() {
    override fun getDisplay() = buildList {
        val mayor = ElectionAPI.currentMayor ?: return@buildList
        val instant = timeUntilNextMayor().toReadableTime()
        val time = if (LinesConfig.showMayorTime) " §7(§e$instant§7)"
        else ""

        add("${candidateColor[mayor]}${mayor.candidateName}$time") {
            hover = listOf("§7Click to open the calendar.")
            command = "/calendar"
        }

        if (LinesConfig.showMayorPerks) {
            mayor.activePerks.forEach {
                addPerk(it)
            }
        }

        if (LinesConfig.showMinister) {
            val minister = ElectionAPI.currentMinister ?: return@buildList

            add("${candidateColor[minister]}${minister.candidateName}")

            if (LinesConfig.showMayorPerks) {
                minister.activePerks.forEach {
                    addPerk(it)
                }
            }
        }
    }

    private fun MutableList<Any>.addPerk(perk: Perk) {
        add(" §7- §e${perk.perkName}") {
            hover = perk.description.splitToWidth(" ", 140).map { "§7$it" }
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override fun showWhen() = ElectionAPI.currentMayor != null

    override val configLine = "Mayor"


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
}
