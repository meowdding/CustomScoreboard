package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.utils.KnownMods
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.mining.GlaciteAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.party.PartyAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object PartyElement : Element() {
    override fun getDisplay() = buildList {
        val list = PartyAPI.members.distinctBy { it.name }

        add(Text.of("Party (${list.size})", TextColor.BLUE)) {
            this.hover = listOf("§7Click to view party info")
            this.command = "/party list"
        }

        if (LinesConfig.showPartyLeader) {
            PartyAPI.leader?.let { leader ->
                val (name, color) = leader.name?.to(TextColor.WHITE) ?: ("Unknown Name" to TextColor.RED)
                val leaderLine = Text.of {
                    append("- ", TextColor.GRAY)
                    append(name, color)
                    append(" ♚", TextColor.YELLOW)
                }
                addMember(leaderLine, name)
            }
        }

        list
            .take(LinesConfig.maxPartyMembers)
            .filter { it.name != null && (!LinesConfig.showPartyLeader || it != PartyAPI.leader) }
            .forEach { member ->
                val (name, color) = member.name?.to(TextColor.WHITE) ?: ("Unknown Name" to TextColor.RED)
                val memberLine = Text.of {
                    append("- ", TextColor.GRAY)
                    append(name, color)
                }
                addMember(memberLine, name)
            }

        if (list.any { it.name == null }) {
            val fixLine = Text.of("Run ") {
                append("/pl", TextColor.GRAY)
                append(" to fix your party")
            }
            add(fixLine) {
                this.hover = listOf("§7Click to run the /pl")
                this.command = "/pl"
            }
        }
    }

    private fun MutableList<Any>.addMember(line: Component, name: String?) {
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
