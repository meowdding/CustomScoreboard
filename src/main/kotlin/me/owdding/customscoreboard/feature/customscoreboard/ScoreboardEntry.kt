package me.owdding.customscoreboard.feature.customscoreboard

import com.teamresourceful.resourcefulconfig.api.types.info.TooltipProvider
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementArea
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBank
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBits
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCold
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCopper
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementDate
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementEvents
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementFooter
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementGems
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementHeat
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementIsland
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementLobby
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMayor
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMotes
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementNorthStars
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementObjective
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementParty
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPet
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPowder
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementProfile
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPurse
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementQuiver
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSeparator
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSlayer
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSoulflow
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTime
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTitle
import me.owdding.customscoreboard.utils.TextUtils.toComponent

enum class ScoreboardEntry(val element: Element) : TooltipProvider {
    SEPARATOR(ElementSeparator),

    TITLE(ElementTitle),
    LOBBY(ElementLobby),
    DATE(ElementDate),
    TIME(ElementTime),
    ISLAND(ElementIsland),
    AREA(ElementArea),
    PROFILE(ElementProfile),

    PURSE(ElementPurse),
    MOTES(ElementMotes),
    BANK(ElementBank),
    BITS(ElementBits),
    COPPER(ElementCopper),
    GEMS(ElementGems),
    HEAT(ElementHeat),
    COLD(ElementCold),
    NORTH_STARS(ElementNorthStars),
    SOULFLOW(ElementSoulflow),

    OBJECTIVE(ElementObjective),
    SLAYER(ElementSlayer),
    EVENTS(ElementEvents),

    //    POWER(Power),
    //    COOKIE(Cookie),
    //    CHUNKED_STATS(ChunkedStats),
    QUIVER(ElementQuiver),
    POWDER(ElementPowder),
    MAYOR(ElementMayor),
    PARTY(ElementParty),
    PET(ElementPet),
    //    Unknown(Unknown),

    FOOTER(ElementFooter),
    ;

    override fun getTooltip() = element.configLineHover.joinToString("\n").toComponent()

    override fun toString() = element.configLine

    companion object {
        val default = listOf(
            TITLE,
            LOBBY,
            SEPARATOR,
            DATE,
            TIME,
            ISLAND,
            AREA,
            PROFILE,
            SEPARATOR,
            PURSE,
            MOTES,
            BANK,
            BITS,
            COPPER,
            GEMS,
            HEAT,
            COLD,
            NORTH_STARS,
            SOULFLOW,
            SEPARATOR,
            OBJECTIVE,
            SLAYER,
            QUIVER,
            EVENTS,
            POWDER,
            MAYOR,
            PARTY,
            PET,
            FOOTER,
        )
    }
}
