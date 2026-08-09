package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.config.category.CustomizationConfig.titleAlignment
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.align
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.rendering.text.serialization.TagComponentSerialization
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.bold
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@ScoreboardElement
object TitleElement : Element() {
    private val titleComponent = Text.of("SkyBlock") {
        color = TextColor.YELLOW
        bold = true
    }

    override fun getDisplay() = when {
        CustomizationConfig.useHypixelTitle -> McClient.scoreboardTitle?.align(titleAlignment)
        !LocationAPI.isOnSkyBlock && !CustomizationConfig.useCustomTitleOutsideSkyBlock -> McClient.scoreboardTitle?.align(titleAlignment)
        CustomizationConfig.titleUseCustomText -> title()
        else -> titleComponent align titleAlignment
    }

    override val configLine = "§e§lSkyBlock"
    override val id = "TITLE"
    override val group = ElementGroup.HEADER

    private fun title() = CustomizationConfig.titleText.map { TagComponentSerialization.deserialize(it) align titleAlignment }
}
