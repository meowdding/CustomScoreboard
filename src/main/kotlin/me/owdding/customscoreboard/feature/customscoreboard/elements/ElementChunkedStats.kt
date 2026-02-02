package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.CustomizationConfig
import me.owdding.customscoreboard.feature.customscoreboard.ChunkedStat
import me.owdding.customscoreboard.utils.ScoreboardElement

@ScoreboardElement
object ElementChunkedStats : Element() {
    override fun getDisplay() = ChunkedStat.getActive().chunked(CustomizationConfig.statsPerLine).map { it.joinToString(" §7| ") { it.display() } }

    override val configLine: String = "Chunked Stats"
    override val id: String = "CHUNKED_STATS"
}
