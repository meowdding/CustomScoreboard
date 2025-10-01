package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.config.categories.LinesConfig
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileType
import tech.thatgravyboat.skyblockapi.utils.text.CommonText
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@AutoElement(ElementGroup.HEADER)
object ElementProfile : Element() {
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


    private val profileSymbol = mapOf(
        ProfileType.IRONMAN to Text.of("♲ ").withColor(TextColor.GRAY),
        ProfileType.STRANDED to Text.of("☀ ").withColor(TextColor.GREEN),
        ProfileType.BINGO to Text.of("Ⓑ ").withColor(ProfileAPI.bingoRank?.color ?: TextColor.AQUA),
        ProfileType.NORMAL to Text.of("").withColor(TextColor.YELLOW),
    )
}
