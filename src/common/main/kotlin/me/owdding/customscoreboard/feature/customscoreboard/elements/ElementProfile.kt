package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileType

@AutoElement(ElementGroup.HEADER)
@ScoreboardElement
object ElementProfile : Element() {
    override fun getDisplay() = buildString {
        val profile = ProfileAPI.profileType
        append(profileSymbol[profile] ?: "§c")
        if (LinesConfig.showProfileName) {
            append(ProfileAPI.profileName)
        } else {
            append(profile)
        }
    }.withActions {
        hover = listOf("§7Click to open the profile switcher")
        command = "/profiles"
    }

    override fun showWhen() = ProfileAPI.profileType != ProfileType.UNKNOWN

    override val configLine = "Profile"
    override val id = "PROFILE"


    private val profileSymbol = mapOf(
        ProfileType.IRONMAN to "§7♲ ",
        ProfileType.STRANDED to "§a☀ ",
        // todo: get actual color using bingoapi
        ProfileType.BINGO to "§bⒷ ",
        ProfileType.NORMAL to "§e",
    )
}
