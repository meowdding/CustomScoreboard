package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileType

object ElementProfile : Element() {
    override fun getDisplay() = buildString {
        val profile = ProfileAPI.profileType
        append(profileSymbol[profile] ?: "§c")
        if (LinesConfig.showProfileName) {
            append(ProfileAPI.profileName)
        } else {
            append(profile)
        }
    }

    override fun showWhen() = ProfileAPI.profileType != ProfileType.UNKNOWN

    override val configLine = "Profile"


    private val profileSymbol = mapOf(
        ProfileType.IRONMAN to "§7♲ ",
        ProfileType.STRANDED to "§a☀ ",
        // todo: get actual color using bingoapi
        ProfileType.BINGO to "§bⒷ ",
        ProfileType.NORMAL to "§e",
    )
}
