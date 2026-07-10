package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.lib.builder.ComponentFactory
import tech.thatgravyboat.skyblockapi.api.area.hub.FarmhouseAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.bold
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@AutoElement
object EventJacobMedals : Event() {
    override fun getDisplay() = ComponentFactory.multiline {
        component {
            color = TextColor.GOLD
            append("GOLD ") { bold = true }
            append("medals: ", TextColor.WHITE)
            append(FarmhouseAPI.goldMedals.toFormattedString())
        }

        component {
            append("SILVER ") { bold = true }
            append("medals: ")
            append(FarmhouseAPI.silverMedals.toFormattedString())
        }

        component {
            color = TextColor.RED
            append("BRONZE ") { bold = true }
            append("medals: ", TextColor.WHITE)
            append(FarmhouseAPI.bronzeMedals.toFormattedString())
        }
    }

    override fun showWhen() = McClient.scoreboard.any(medalRegex::matches)

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.HUB, SkyBlockIsland.GARDEN)

    override val configLine = "Jacob Medals"

    private val medalRegex by RemoteStrings.resolve().componentRegex(".* medals: \\d+")
}
