package me.owdding.customscoreboard.feature.customscoreboard

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBank
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBits
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCold
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCopper
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementGems
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementHeat
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMotes
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementNorthStars
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPurse
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSowdust
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.area.mining.HollowsAPI
import tech.thatgravyboat.skyblockapi.api.area.rift.RiftAPI
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI

enum class ChunkedStat(val display: () -> String, val element: Element) {
    PURSE({ "§6${CurrencyAPI.purse.format()}" }, ElementPurse),
    MOTES({ "§d${RiftAPI.motes.format()}" }, ElementMotes),
    BANK({ "§6${ElementBank.line()}" }, ElementBank),
    BITS({ "§b${ElementBits.line()}" }, ElementBits),
    COPPER({ "§c${CurrencyAPI.copper.format()}" }, ElementCopper),
    SOWDUST({ "§2${CurrencyAPI.sowdust.format()}" }, ElementSowdust),
    GEMS({ "§a${CurrencyAPI.gems.format()}" }, ElementGems),
    HEAT({ if (HollowsAPI.immuneToHeat) "§6IMMUNE" else "§c${HollowsAPI.heat}" }, ElementHeat),
    COLD({ "§b${GlaciteAPI.cold.format()}" }, ElementCold),
    NORTH_STARS({ "§d${CurrencyAPI.northStars.format()}" }, ElementNorthStars),
    ;

    override fun toString(): String = element.configLine

    companion object {
        fun getActive() = MainConfig.chunkedStats.filter { it.element.showWhen() && it.element.showIsland() }.toList()
    }
}
