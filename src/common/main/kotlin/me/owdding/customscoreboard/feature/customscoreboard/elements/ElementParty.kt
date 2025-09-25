package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.utils.KnownMods
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.party.PartyAPI

@ScoreboardElement
object ElementParty : Element() {
    override fun getDisplay() = buildList {
        val list = PartyAPI.members.distinctBy { it.name }
        add("§9Party (${list.size})") {
            this.hover = listOf("§7Click to view party info")
            this.command = "/party list"
        }
        if (LinesConfig.showPartyLeader) {
            PartyAPI.leader?.let {
                addMember("§7- §f${it.name ?: "§cUnknown"} §e♚", it.name)
            }
        }

        list
            .take(LinesConfig.maxPartyMembers)
            .filter { LinesConfig.showPartyLeader && it != PartyAPI.leader && it.name != null }
            .forEach {
                addMember("§7- §f${it.name}", it.name)
            }

        if (list.any { it.name == null }) {
            add("§fRun §7/pl §fto fix your party") {
                this.hover = listOf("§7Click to run the /pl")
                this.command = "/pl"
            }
        }
    }

    private fun MutableList<Any>.addMember(line: String, name: String?) {
        if ((KnownMods.SKYBLOCK_PV.installed || KnownMods.SKYBLOCKER.installed)) {
            add(line) {
                this.hover = listOf("§7Click to view ${name}'s profile")
                this.command = "/pv $name"
            }
        } else add(line) {
            this.hover = listOf("§7Click to open SkyCrypt.")
            this.link = "https://sky.shiiyu.moe/stats/${name}"
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
    override val id = "PARTY"
}
