package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.area.rift.RiftAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object ElementMotes : NumberTrackingElement(TextColor.PINK) {

    override fun getDisplay(): Component {
        checkDifference(RiftAPI.motes)
        val line = Text.join(RiftAPI.motes.format(), temporaryChangeDisplay)

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Motes", line, numberColor)
    }

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)
    override fun isLineActive() = RiftAPI.motes > 0

    override val configLine = "Motes"
    override val id = "MOTES"
}
