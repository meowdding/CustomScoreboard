package me.owdding.customscoreboard.core

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.elements.BankElement
import me.owdding.customscoreboard.elements.BitsElement
import me.owdding.customscoreboard.elements.ColdElement
import me.owdding.customscoreboard.elements.CopperElement
import me.owdding.customscoreboard.elements.Element
import me.owdding.customscoreboard.elements.GemsElement
import me.owdding.customscoreboard.elements.HeatElement
import me.owdding.customscoreboard.elements.MotesElement
import me.owdding.customscoreboard.elements.NorthStarsElement
import me.owdding.customscoreboard.elements.PurseElement
import me.owdding.customscoreboard.elements.SowdustElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.area.mining.HollowsAPI
import tech.thatgravyboat.skyblockapi.api.area.rift.RiftAPI
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI

enum class ChunkedStat(val display: () -> String, val element: Element) {
    PURSE({ "§6${CurrencyAPI.purse.format()}" }, PurseElement),
    MOTES({ "§d${RiftAPI.motes.format()}" }, MotesElement),
    BANK({ "§6${BankElement.line()}" }, BankElement),
    BITS({ "§b${BitsElement.line()}" }, BitsElement),
    COPPER({ "§c${CurrencyAPI.copper.format()}" }, CopperElement),
    SOWDUST({ "§2${CurrencyAPI.sowdust.format()}" }, SowdustElement),
    GEMS({ "§a${CurrencyAPI.gems.format()}" }, GemsElement),
    HEAT({ if (HollowsAPI.immuneToHeat) "§6IMMUNE" else "§c${HollowsAPI.heat}" }, HeatElement),
    COLD({ "§b${GlaciteAPI.cold.format()}" }, ColdElement),
    NORTH_STARS({ "§d${CurrencyAPI.northStars.format()}" }, NorthStarsElement),
    ;

    override fun toString(): String = element.configLine

    companion object {
        fun getActive() = CustomizationConfig.chunkedStats.filter { it.element.showWhen() && it.element.showIsland() }.toList()
    }
}
