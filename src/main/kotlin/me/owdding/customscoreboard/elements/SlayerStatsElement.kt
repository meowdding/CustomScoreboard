package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.elements.SlayerElement.isInSlayerRegion
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.lib.builder.ComponentFactory
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.slayer.SlayerAPI
import tech.thatgravyboat.skyblockapi.api.profile.slayer.SlayerProgressAPI
import tech.thatgravyboat.skyblockapi.api.remote.repo.RepoSlayerData
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object SlayerStatsElement : Element() {
    override fun getDisplay(): Component? {
        val type = SlayerAPI.type ?: return null
        val data = SlayerProgressAPI.slayerData.entries.find { it.key == type } ?: return null
        val repo = RepoSlayerData.getData(type)

        return ComponentFactory.multiline {
            string("Slayer Stats") {
                if (LinesConfig.slayerLevel) {
                    append(" (", TextColor.GRAY)
                    append(repo.getLevel(data.value.xp).toString(), TextColor.RED)
                    append(")", TextColor.GRAY)
                }
            }

            string(" Xp: ") {
                append(data.value.xp.format(), TextColor.RED)
            }

            string(" Meter: ") {
                append(data.value.meterXp.format(), TextColor.LIGHT_PURPLE)
            }
        }
    }

    override fun showWhen(): Boolean = !LinesConfig.hideSlayerOutsideSlayerAreas || isInSlayerRegion()

    override val configLine: String = "Slayer Stats"
    override val id = "SLAYER_STATS"
    override val configLineHover = listOf(
        "§7The current slayer xp (and level) and meter xp you have.",
        "§7Will only show when Hypixel shows an active slayer quest.",
    )
}
