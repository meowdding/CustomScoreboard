package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileType
import tech.thatgravyboat.skyblockapi.utils.text.CommonText
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ProfileElement : Element() {
    override fun getDisplay() = Text.of {
        val profile = ProfileAPI.profileType
        append(profileSymbol[profile] ?: CommonText.EMPTY) {
            if (LinesConfig.showProfileName) {
                append(ProfileAPI.profileName ?: "Unknown")
            } else {
                append(profile.toString())
            }
        }
    }.withActions {
        hover = listOf("§7Click to open the profile switcher")
        command = "/profiles"
    }

    override fun showWhen() = ProfileAPI.profileType != ProfileType.UNKNOWN

    override val configLine = "Profile"
    override val id = "PROFILE"
    override val group = ElementGroup.HEADER


    private val profileSymbol = mapOf(
        ProfileType.IRONMAN to Text.of("♲ ", TextColor.GRAY),
        ProfileType.STRANDED to Text.of("☀ ", TextColor.GREEN),
        ProfileType.BINGO to Text.of("Ⓑ ", ProfileAPI.bingoRank?.color ?: TextColor.AQUA),
        ProfileType.NORMAL to Text.of("", TextColor.YELLOW),
    )
}
