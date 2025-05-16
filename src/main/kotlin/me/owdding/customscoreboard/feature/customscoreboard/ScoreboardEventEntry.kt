package me.owdding.customscoreboard.feature.customscoreboard

import com.teamresourceful.resourcefulconfig.api.types.info.TooltipProvider
import me.owdding.customscoreboard.feature.customscoreboard.events.Event
import me.owdding.customscoreboard.feature.customscoreboard.events.EventBroodmother
import me.owdding.customscoreboard.feature.customscoreboard.events.EventCarnival
import me.owdding.customscoreboard.feature.customscoreboard.events.EventDamage
import me.owdding.customscoreboard.feature.customscoreboard.events.EventDarkAuction
import me.owdding.customscoreboard.feature.customscoreboard.events.EventDojo
import me.owdding.customscoreboard.feature.customscoreboard.events.EventDungeons
import me.owdding.customscoreboard.feature.customscoreboard.events.EventEssence
import me.owdding.customscoreboard.feature.customscoreboard.events.EventFlightDuration
import me.owdding.customscoreboard.feature.customscoreboard.events.EventGarden
import me.owdding.customscoreboard.feature.customscoreboard.events.EventJacobMedals
import me.owdding.customscoreboard.feature.customscoreboard.events.EventJacobsContest
import me.owdding.customscoreboard.feature.customscoreboard.events.EventKuudra
import me.owdding.customscoreboard.feature.customscoreboard.events.EventMagmaBoss
import me.owdding.customscoreboard.feature.customscoreboard.events.EventMining
import me.owdding.customscoreboard.feature.customscoreboard.events.EventNewYear
import me.owdding.customscoreboard.feature.customscoreboard.events.EventQueue
import me.owdding.customscoreboard.feature.customscoreboard.events.EventRedstone
import me.owdding.customscoreboard.feature.customscoreboard.events.EventRift
import me.owdding.customscoreboard.feature.customscoreboard.events.EventServerRestart
import me.owdding.customscoreboard.feature.customscoreboard.events.EventSpooky
import me.owdding.customscoreboard.feature.customscoreboard.events.EventTrapper
import me.owdding.customscoreboard.feature.customscoreboard.events.EventVoting
import me.owdding.customscoreboard.feature.customscoreboard.events.EventWinter
import me.owdding.customscoreboard.utils.TextUtils.toComponent

enum class ScoreboardEventEntry(val event: Event) : TooltipProvider {
    VOTING(EventVoting),
    SERVER_RESTART(EventServerRestart),
    DUNGEONS(EventDungeons),
    KUUDRA(EventKuudra),
    DOJO(EventDojo),
    DARK_AUCTION(EventDarkAuction),
    JACOB_CONTEST(EventJacobsContest),
    JACOB_MEDALS(EventJacobMedals),
    TRAPPER(EventTrapper),
    GARDEN(EventGarden),
    FLIGHT_DURATION(EventFlightDuration),
    WINTER(EventWinter),
    NEW_YEAR(EventNewYear),
    SPOOKY(EventSpooky),
    BROODMOTHER(EventBroodmother),
    MINING_EVENTS(EventMining),
    DAMAGE(EventDamage),
    MAGMA_BOSS(EventMagmaBoss),
    CARNIVAL(EventCarnival),
    RIFT(EventRift),
    ESSENCE(EventEssence),
    QUEUE(EventQueue),
    REDSTONE(EventRedstone),


//     ACTIVE_TABLIST_EVENTS(ScoreboardEventActiveTablist),
//     STARTING_SOON_TABLIST_EVENTS(ScoreboardEventStartingSoonTablist),
    ;

    override fun getTooltip() = event.configLineHover.joinToString("\n").toComponent()

    override fun toString() = event.configLine
}
