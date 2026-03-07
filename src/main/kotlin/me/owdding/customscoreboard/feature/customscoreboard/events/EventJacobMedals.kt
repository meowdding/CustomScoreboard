package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import tech.thatgravyboat.skyblockapi.api.area.hub.FarmhouseAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString

@AutoElement
object EventJacobMedals : Event() {
    override fun getDisplay() = buildList {
        add("§6§lGOLD §fmedals: §6${FarmhouseAPI.goldMedals.toFormattedString()}")
        add("§lSILVER §fmedals: ${FarmhouseAPI.silverMedals.toFormattedString()}")
        add("§c§lBRONZE §fmedals: §c${FarmhouseAPI.bronzeMedals.toFormattedString()}")
    }

    override fun showWhen() = McClient.scoreboard.any(medalRegex::matches)

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.HUB, SkyBlockIsland.GARDEN)

    override val configLine = "Jacob Medals"

    private val medalRegex by RemoteStrings.resolve().componentRegex(".* medals: \\d+")
}
