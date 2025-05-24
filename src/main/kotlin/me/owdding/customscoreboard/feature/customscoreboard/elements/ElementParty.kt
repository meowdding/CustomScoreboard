package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.party.PartyAPI

@AutoElement
object ElementParty : Element() {
    override fun getDisplay() = buildList {
        val list = PartyAPI.members.distinctBy { it.name }
        add("§9Party (${list.size})") {
            this.hover = listOf("§7Click to view party info")
            this.clickCommand = "/party info"
        }

        add(
            "§9Party (${list.size})".withActions {
                this.hover = listOf("§7Click to view party info")
                this.clickCommand = "/party info"
            },
        )
        if (LinesConfig.showPartyLeader) {
            PartyAPI.leader?.let {
                add("§7- §f${it.name ?: "§cUnknown"} §e♚")
            }
        }

        list
            .take(LinesConfig.maxPartyMembers)
            .filter { LinesConfig.showPartyLeader && it != PartyAPI.leader }
            .forEach {
                add("§7- §f${it.name ?: "§cUnknown"}")
            }

        if (list.any { it.name == null }) {
            add("§fRun §7/pl §fto fix your party")
        }
    }

    override fun showWhen() = PartyAPI.size > 0 && when {
        SkyBlockIsland.THE_CATACOMBS.inIsland() -> false
        LinesConfig.showPartyEverywhere -> true
        else -> SkyBlockIsland.inAnyIsland(
            SkyBlockIsland.DUNGEON_HUB,
            SkyBlockIsland.CRIMSON_ISLE,
        ) || GlaciteAPI.inGlaciteTunnels()
    }

    override val configLine = "Party"
}
