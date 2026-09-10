package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.elements.MayorElement.addHoverPerks
import me.owdding.customscoreboard.elements.MayorElement.addPerks
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.extensions.toReadableTime
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.hub.ElectionAPI
import tech.thatgravyboat.skyblockapi.api.data.MayorCandidate
import tech.thatgravyboat.skyblockapi.api.data.MayorCandidates
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockInstant
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.extentions.until
import tech.thatgravyboat.skyblockapi.utils.text.CommonText
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextUtils.splitToWidth
import kotlin.time.Duration

@ScoreboardElement
object MayorElement : Element() {
    override fun getDisplay() = buildList {
        val jerryActive = MayorCandidates.JERRY.isActive && LinesConfig.showJerryInMinister && ElectionAPI.currentJerryCandidate != null
        val mayor = ElectionAPI.mayor ?: return@buildList

        val perksDisplay = LinesConfig.mayorPerksDisplay
        val ministerDisplay = LinesConfig.ministerDisplay
        val minister = ElectionAPI.minister ?: if (jerryActive) ElectionAPI.currentJerryCandidate?.first else null

        val mainLine = Text.of {
            append(mayor.formatName())

            if (perksDisplay == PerkDisplay.COUNT) {
                append(" (${mayor.activePerks.size})", TextColor.YELLOW)
            }

            if (ministerDisplay == MinisterDisplay.COMPACT && minister != null) {
                append(", ", TextColor.GRAY)
                append(minister.formatName())
            }

            if (LinesConfig.showMayorTime) {
                append(" (", TextColor.GRAY)
                append(timeUntilNextMayor().toReadableTime(), TextColor.YELLOW)
                if (jerryActive && ministerDisplay == MinisterDisplay.COMPACT) {
                    append(", ", TextColor.GRAY)
                    append(timeUntilJerryMayor()?.toReadableTime() ?: "", TextColor.YELLOW)
                }
                append(")", TextColor.GRAY)
            }
        }

        add(mainLine) {
            if (perksDisplay != PerkDisplay.ALL) hover(
                buildList<Component> {
                    addHoverPerks(mayor)
                    if (minister != null && ministerDisplay == MinisterDisplay.COMPACT) {
                        add(CommonText.EMPTY)
                        addHoverPerks(minister)
                    }
                    add(CommonText.EMPTY)
                    add(CommonText.EMPTY)
                    add(Text.of("Click to open the calendar.", TextColor.YELLOW))
                },
            ) else hover(Text.of("Click to open the calendar.", TextColor.GRAY))
            command = "/calendar"
        }

        if (perksDisplay == PerkDisplay.ALL) {
            addPerks(mayor)
        }

        if (minister != null && ministerDisplay == MinisterDisplay.FULL) {
            val ministerLine = Text.of {
                append(minister.formatName())
                if (jerryActive) {
                    append(" (", TextColor.GRAY)
                    append(timeUntilJerryMayor()?.toReadableTime() ?: "", TextColor.YELLOW)
                    append(")", TextColor.GRAY)
                }
            }

            add(ministerLine) {
                if (perksDisplay == PerkDisplay.OFF) {
                    hover(buildList<Component> { addHoverPerks(minister) })
                }
            }

            if (perksDisplay == PerkDisplay.ALL) {
                addPerks(minister)
            }
        }
    }

    private fun MutableList<Any>.addPerks(candidate: MayorCandidate) {
        val color = candidateColor[candidate] ?: TextColor.YELLOW
        candidate.activePerks.forEach { perk ->
            val perkLine = Text.of {
                append(" - ", TextColor.GRAY)
                append(perk.perkName, color)
            }

            add(perkLine) {
                hover(perk.description.splitToWidth(" ", 140).map { Text.of(it, TextColor.GRAY) })
            }
        }
    }

    private fun MutableList<Component>.addHoverPerks(candidate: MayorCandidate) {
        val color = candidateColor[candidate] ?: TextColor.YELLOW
        candidate.activePerks.forEachIndexed { i, perk ->
            if (i != 0) add(CommonText.EMPTY)
            add(Text.of("${perk.perkName}:", color))
            perk.description.splitToWidth(" ", 140).mapTo(this) { Text.of("  $it", TextColor.GRAY) }
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

    private fun MayorCandidate.formatName(): Component = Text.of(candidateName, candidateColor[this] ?: TextColor.YELLOW)

    private val candidateColor = mapOf(
        MayorCandidates.AATROX to TextColor.DARK_AQUA,
        MayorCandidates.COLE to TextColor.YELLOW,
        MayorCandidates.DIANA to TextColor.DARK_GREEN,
        MayorCandidates.DIAZ to TextColor.GOLD,
        MayorCandidates.FINNEGAN to TextColor.RED,
        MayorCandidates.FOXY to TextColor.PINK,
        MayorCandidates.MARINA to TextColor.AQUA,
        MayorCandidates.PAUL to TextColor.RED,
        MayorCandidates.SCORPIUS to TextColor.PINK,
        MayorCandidates.JERRY to TextColor.PINK,
        MayorCandidates.DERPY to TextColor.PINK,
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
