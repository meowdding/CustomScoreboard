package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.TextUtils.toComponent
import net.minecraft.world.item.Items
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.api.profile.quiver.QuiverAPI
import tech.thatgravyboat.skyblockapi.api.remote.api.SkyBlockId
import tech.thatgravyboat.skyblockapi.api.remote.api.SkyBlockId.Companion.getSkyBlockId
import tech.thatgravyboat.skyblockapi.helpers.McPlayer
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedName
import tech.thatgravyboat.skyblockapi.utils.extentions.toTitleCase
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@ScoreboardElement
object QuiverElement : Element() {
    private const val MAX_ARROW_AMOUNT = 2880
    private val ID = SkyBlockId.item("SKELETON_MASTER_CHESTPLATE")

    override fun getDisplay(): Any? {
        val type = QuiverAPI.currentArrow?.toTitleCase()?.toComponent() ?: return null
        val amount = QuiverAPI.currentAmount ?: return null

        val percentage = amount / MAX_ARROW_AMOUNT.toDouble() * 100

        val color = if (LinesConfig.colorArrowAmount) {
            when {
                percentage <= 10 -> TextColor.RED
                percentage <= 25 -> TextColor.ORANGE
                percentage <= 50 -> TextColor.YELLOW
                percentage <= 75 -> TextColor.DARK_GREEN
                else -> TextColor.GREEN
            }
        } else TextColor.WHITE

        val wearingSkeletonMasterChestplate = McPlayer.chestplate.getSkyBlockId() == ID

        val amountLine = if (wearingSkeletonMasterChestplate) {
            "∞"
        } else {
            when (LinesConfig.arrowDisplay) {
                ArrowDisplay.NUMBER -> amount.toString()
                ArrowDisplay.PERCENTAGE -> "${percentage.toInt()}%"
            }
        }.toComponent()

        val element = CustomScoreboardRenderer.formatNumberDisplayDisplay(type, amountLine, color)
        return if (!EffectsAPI.isBoosterCookieActive) element else element.withActions {
            hover = listOf("§7Click to open the quiver")
            command = "/quiver"
        }
    }

    override fun showWhen() = McPlayer.inventory.any { it.item == Items.BOW }
    override fun isLineActive() = QuiverAPI.currentAmount != null && QuiverAPI.currentAmount!! > 0

    override val configLine = "Quiver"
    override val id = "QUIVER"

    enum class ArrowDisplay {
        NUMBER,
        PERCENTAGE,
        ;

        private val formattedName = toFormattedName()
        override fun toString() = formattedName
    }
}
