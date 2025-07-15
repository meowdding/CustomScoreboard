package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.area.rift.RiftAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@AutoElement
object ElementMotes : Element(), NumberTrackingElement {
    override var previousAmount: Long = -1
    override var temporaryChangeDisplay: String? = null
    override val numberColor = "§d"

    override fun getDisplay(): String {
        checkDifference(RiftAPI.motes)
        val line = RiftAPI.motes.format() + temporaryChangeDisplay.orEmpty()

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Motes", line, numberColor)
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override val configLine = "Motes"
}
