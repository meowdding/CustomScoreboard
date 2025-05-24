package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.lib.utils.KnownMods
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.party.PartyAPI

@AutoElement
object ElementParty : Element() {
    override fun getDisplay() = buildList {
        val list = PartyAPI.members.distinctBy { it.name }
        add("§9Party (${list.size})") {
            this.hover = listOf("§7Click to view party info")
            this.command = "/party list"
        }
        if (LinesConfig.showPartyLeader) {
            PartyAPI.leader?.let {
                addPerson("§7- §f${it.name ?: "§cUnknown"} §e♚", it.name)
            }
        }

        list
            .take(LinesConfig.maxPartyMembers)
            .filter { LinesConfig.showPartyLeader && it != PartyAPI.leader }
            .forEach {
                val line = "§7- §f${it.name ?: "§cUnknown"}"
                if ((KnownMods.SKYBLOCK_PV.installed || KnownMods.SKYBLOCKER.installed) && it.name != null) {
                    addPerson(line, it.name)
                } else add(line) {
                    this.hover = listOf("§7Click to open SkyCrypt.")
                    this.link = "https://sky.shiiyu.moe/stats/${it.name}"
                }
            }

        if (list.any { it.name == null }) {
            add("§fRun §7/pl §fto fix your party") {
                this.hover = listOf("§7Click to run the /pl")
                this.command = "/pl"
            }
        }
    }

    private fun MutableList<Any>.addPerson(line: String, name: String?) {
        add(line) {
            this.hover = listOf("§7Click to view ${name ?: "§cUnknown"}'s profile")
            if (name != null) this.command = "/pv $name"
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
