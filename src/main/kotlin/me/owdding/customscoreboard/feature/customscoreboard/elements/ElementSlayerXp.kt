package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import tech.thatgravyboat.skyblockapi.api.area.slayer.SlayerAPI
import tech.thatgravyboat.skyblockapi.api.profile.slayer.SlayerProgressAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString

@AutoElement
object ElementSlayerXp : Element() {
    override fun getDisplay() = buildList<Any?> {
        val type = SlayerAPI.type ?: return@buildList
        val data = SlayerProgressAPI.slayerData.entries.find { it.key == type } ?: return@buildList

        add("Slayer Progress")
        add(" Xp: §c${data.value.xp.toFormattedString()}")
        add(" Meter: §d${data.value.meterXp.toFormattedString()}")
    }

    override val configLine: String = "Slayer XP"
}
