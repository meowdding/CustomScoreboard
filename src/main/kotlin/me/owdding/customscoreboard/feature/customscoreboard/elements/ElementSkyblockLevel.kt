package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.wrap
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@AutoElement(ElementGroup.MIDDLE)
object ElementSkyblockLevel : Element() {

    override fun getDisplay() = Text.of {
        append("SB Lvl: ")
        append(Text.of(ProfileAPI.sbLevel.toString()) { this.color = ProfileAPI.getLevelColor() }.wrap("[", "]").withColor(TextColor.DARK_GRAY))
        append(" ")
        append(
            Text.of {
                append(ProfileAPI.sbLevelProgress) { this.color = TextColor.AQUA }
                append("/") { this.color = TextColor.DARK_AQUA }
                append("100") { this.color = TextColor.AQUA }
            }.wrap("(", ")").withColor(TextColor.GRAY),
        )
    }.withActions {
        hover = listOf("§7Click to open SkyBlock Level Menu")
        command = "/skyblocklevels"
    }

    override val configLine = "SB Level"
}
