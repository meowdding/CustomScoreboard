package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.wrap
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object SkyblockLevelElement : Element() {

    override fun getDisplay() = Text.of {
        append("SB Lvl: ")
        append(Text.of(ProfileAPI.sbLevel.toString(), ProfileAPI.getLevelColor()).wrap("[", "]").withColor(TextColor.DARK_GRAY))
        append(" ")
        append(
            Text.of {
                append(ProfileAPI.sbLevelProgress.toString(), TextColor.AQUA)
                append("/", TextColor.DARK_AQUA)
                append("100", TextColor.AQUA)
            }.wrap("(", ")").withColor(TextColor.GRAY),
        )
    }.withActions {
        hover = listOf("§7Click to open SkyBlock Level Menu")
        command = "/skyblocklevels"
    }

    override val configLine = "SB Level"
    override val id = "SKYBLOCK_LEVEL"
}
