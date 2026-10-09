package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.extensions.ordinal
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.environmental.DateTimeAPI
import tech.thatgravyboat.skyblockapi.api.environmental.SkyBlockSeason
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color
import kotlin.collections.find

@ScoreboardElement
object DateElement : Element() {
    override fun getDisplay() = Text.of("${DateTimeAPI.season} ${DateTimeAPI.day}${DateTimeAPI.day.ordinal()}") {
        color = seasonColors[DateTimeAPI.season]?.takeIf { LinesConfig.coloredMonth } ?: TextColor.WHITE
    }.withActions {
        val buff = atmosphericEffect.entries.find { DateTimeAPI.season in it.key }?.value ?: return@withActions
        hover(
            buildList<Component> {
                add(Text.of("Atmospheric Filter Buff", TextColor.BLUE))
                add(buff)
            },
        )
    }

    override fun showWhen() = DateTimeAPI.season != null
    override fun shouldIndent(): Boolean = LinesConfig.indentLocationTimeLines

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

    private val atmosphericEffect = mapOf(
        listOf(SkyBlockSeason.EARLY_SPRING, SkyBlockSeason.SPRING, SkyBlockSeason.LATE_SPRING) to Text.of {
            color = TextColor.GRAY
            append("Grants ")
            append("+25\uE051 Farming Fortune", TextColor.GOLD)
            append(".")
        },
        listOf(SkyBlockSeason.EARLY_SUMMER, SkyBlockSeason.SUMMER, SkyBlockSeason.LATE_SUMMER) to Text.of {
            color = TextColor.GRAY
            append("Grants ")
            append("+20☯ Farming Wisdom", TextColor.DARK_AQUA)
            append(".")
        },
        listOf(SkyBlockSeason.EARLY_AUTUMN, SkyBlockSeason.AUTUMN, SkyBlockSeason.LATE_AUTUMN) to Text.of {
            color = TextColor.GRAY
            append("\uE07F Pests", TextColor.DARK_GREEN)
            append("spawn ")
            append("15% ", TextColor.GREEN)
            append("more often.")
        },
        listOf(SkyBlockSeason.EARLY_WINTER, SkyBlockSeason.WINTER, SkyBlockSeason.LATE_WINTER) to Text.of {
            color = TextColor.GRAY
            append("Gain ")
            append("5% ", TextColor.GREEN)
            append("more ")
            append("Copper ", TextColor.RED)
            append("from ")
            append("Garden Visitors", TextColor.GREEN)
            append(".")
        },
    )
}
