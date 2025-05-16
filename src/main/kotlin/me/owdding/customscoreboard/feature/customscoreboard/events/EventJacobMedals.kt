package me.owdding.customscoreboard.feature.customscoreboard.events

import tech.thatgravyboat.skyblockapi.api.area.hub.FarmhouseAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

object EventJacobMedals : Event() {
    override fun getDisplay() = buildList {
        add("§6§lGOLD §fmedals: §6${FarmhouseAPI.goldMedals.toFormattedString()}")
        add("§lSILVER §fmedals: ${FarmhouseAPI.silverMedals.toFormattedString()}")
        add("§c§lBRONZE §fmedals: §c${FarmhouseAPI.bronzeMedals.toFormattedString()}")
    }

    override fun showWhen() = McClient.scoreboard.any { medalRegex.matches(it.stripped) }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.HUB, SkyBlockIsland.GARDEN)

    override val configLine = "Jacob Medals"


    private val medalRegex = ".* medals: \\d+".toRegex()
}
