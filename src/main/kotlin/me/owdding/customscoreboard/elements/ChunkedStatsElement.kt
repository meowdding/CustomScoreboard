package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.core.ChunkedStat
import me.owdding.customscoreboard.utils.ScoreboardElement

@ScoreboardElement
object ChunkedStatsElement : Element() {
    override fun getDisplay() = ChunkedStat.getActive().chunked(CustomizationConfig.statsPerLine).map { it.joinToString(" §7| ") { it.display() } }

    override val configLine: String = "Chunked Stats"
    override val id: String = "CHUNKED_STATS"
}
