package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.core.ChunkedStat
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.TextUtils.joinToComponent
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ChunkedStatsElement : Element() {
    override fun getDisplay() = ChunkedStat.getActive().chunked(CustomizationConfig.statsPerLine).map { line ->
        line.joinToComponent(Text.of(" | ", TextColor.GRAY)) { it.display() }
    }

    override val configLine: String = "Chunked Stats"
    override val id: String = "CHUNKED_STATS"
}
