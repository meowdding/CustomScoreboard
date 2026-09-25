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
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.area.mining.HollowsAPI
import tech.thatgravyboat.skyblockapi.api.area.rift.RiftAPI
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

enum class ChunkedStat(val display: () -> Component, val element: Element) {
    PURSE({ Text.of(CurrencyAPI.purse.format(), TextColor.GOLD) }, PurseElement),
    MOTES({ Text.of(RiftAPI.motes.format(), TextColor.LIGHT_PURPLE) }, MotesElement),
    BANK({ Text.of(BankElement.line(), TextColor.GOLD) }, BankElement),
    BITS({ Text.of(BitsElement.line(), TextColor.AQUA) }, BitsElement),
    COPPER({ Text.of(CurrencyAPI.copper.format(), TextColor.RED) }, CopperElement),
    SOWDUST({ Text.of(CurrencyAPI.sowdust.format(), TextColor.DARK_GREEN) }, SowdustElement),
    GEMS({ Text.of(GemsElement.format(CurrencyAPI.gems), TextColor.GREEN) }, GemsElement),
    HEAT(
        {
            if (HollowsAPI.immuneToHeat) Text.of("IMMUNE", TextColor.GOLD)
            else Text.of(HollowsAPI.heat.toString(), TextColor.RED)
        },
        HeatElement,
    ),
    COLD({ Text.of(GlaciteAPI.cold.format(), TextColor.AQUA) }, ColdElement),
    NORTH_STARS({ Text.of(CurrencyAPI.northStars.format(), TextColor.LIGHT_PURPLE) }, NorthStarsElement),
    ;

    override fun toString(): String = element.configLine

    companion object {
        fun getActive() = CustomizationConfig.chunkedStats.filter { it.element.showWhen() && it.element.showIsland() }.toList()
    }
}
