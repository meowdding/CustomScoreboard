package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ScoreboardElement
object ElementIrlTime : Element() {
    private val format12h = DateTimeFormatter.ofPattern("hh:mma")
    private val format24h = DateTimeFormatter.ofPattern("HH:mm")

    override fun getDisplay() = Text.of {
        color = TextColor.GRAY
        val formatter = if (LinesConfig.time24hFormat) format24h else format12h
        append(LocalDateTime.now().format(formatter).lowercase())
    }

    override val configLine = "IRL Time"
    override val id = "IRL_TIME"
    override val group = ElementGroup.HEADER
}
