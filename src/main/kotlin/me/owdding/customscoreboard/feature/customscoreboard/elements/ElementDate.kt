package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.extensions.ordinal
import tech.thatgravyboat.skyblockapi.api.datetime.DateTimeAPI
import tech.thatgravyboat.skyblockapi.api.datetime.SkyBlockSeason
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@ScoreboardElement
object ElementDate : Element() {
    override fun getDisplay() = Text.of("${DateTimeAPI.season} ${DateTimeAPI.day}${DateTimeAPI.day.ordinal()}") {
        color = seasonColors[DateTimeAPI.season]?.takeIf { LinesConfig.coloredMonth } ?: TextColor.WHITE
    }

    override fun showWhen() = DateTimeAPI.season != null

    override val configLine = "Date"
    override val id = "DATE"
    override val group = ElementGroup.HEADER


    private val seasonColors = mapOf(
        SkyBlockSeason.EARLY_SPRING to TextColor.GREEN,
        SkyBlockSeason.SPRING to TextColor.GREEN,
        SkyBlockSeason.LATE_SPRING to TextColor.GREEN,
        SkyBlockSeason.EARLY_SUMMER to TextColor.YELLOW,
        SkyBlockSeason.SUMMER to TextColor.YELLOW,
        SkyBlockSeason.LATE_SUMMER to TextColor.YELLOW,
        SkyBlockSeason.EARLY_AUTUMN to TextColor.ORANGE,
        SkyBlockSeason.AUTUMN to TextColor.ORANGE,
        SkyBlockSeason.LATE_AUTUMN to TextColor.ORANGE,
        SkyBlockSeason.EARLY_WINTER to TextColor.AQUA,
        SkyBlockSeason.WINTER to TextColor.AQUA,
        SkyBlockSeason.LATE_WINTER to TextColor.AQUA,
    )
}
