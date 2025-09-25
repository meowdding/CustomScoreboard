package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.area.slayer.SlayerAPI
import tech.thatgravyboat.skyblockapi.api.profile.slayer.SlayerProgressAPI
import tech.thatgravyboat.skyblockapi.api.remote.repo.RepoSlayerData

@ScoreboardElement
object ElementSlayerStats : Element() {
    override fun getDisplay() = buildList<Any?> {
        val type = SlayerAPI.type ?: return@buildList
        val data = SlayerProgressAPI.slayerData.entries.find { it.key == type } ?: return@buildList
        val repo = RepoSlayerData.getData(type)

        val slayerLevel = " §7(§c${repo.getLevel(data.value.xp)}§7)".takeIf { LinesConfig.slayerLevel } ?: ""
        add("Slayer Stats$slayerLevel")
        add(" ${CustomScoreboardRenderer.formatNumberDisplayDisplay("Xp", data.value.xp.format(), "§c")}")
        add(" ${CustomScoreboardRenderer.formatNumberDisplayDisplay("Meter", data.value.meterXp.format(), "§d")}")
    }

    override val configLine: String = "Slayer Stats"
    override val id = "SLAYER_STATS"
    override val configLineHover = listOf(
        "§7The current slayer xp (and level) and meter xp you have.",
        "§7Will only show when Hypixel shows an active slayer quest.",
    )
}
