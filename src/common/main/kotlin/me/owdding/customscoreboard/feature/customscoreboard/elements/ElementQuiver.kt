package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.Utils.hasCookieActive
import net.minecraft.world.item.Items
import tech.thatgravyboat.skyblockapi.api.profile.quiver.QuiverAPI
import tech.thatgravyboat.skyblockapi.helpers.McPlayer
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedName
import tech.thatgravyboat.skyblockapi.utils.extentions.toTitleCase

@ScoreboardElement
object ElementQuiver : Element() {
    private const val MAX_ARROW_AMOUNT = 2880

    override fun getDisplay(): Any? {
        val type = QuiverAPI.currentArrow?.toTitleCase() ?: return null
        val amount = QuiverAPI.currentAmount ?: return null

        val percentage = amount / MAX_ARROW_AMOUNT.toDouble() * 100

        val color = if (LinesConfig.colorArrowAmount) {
            when {
                percentage <= 10 -> "§c"
                percentage <= 25 -> "§6"
                percentage <= 50 -> "§e"
                percentage <= 75 -> "§2"
                else -> "§a"
            }
        } else ""

        val wearingSkeletonMasterChestplate = McPlayer.chestplate.hoverName.string.contains("Skeleton Master Chestplate")

        val amountLine = if (wearingSkeletonMasterChestplate) {
            "∞"
        } else {
            when (LinesConfig.arrowDisplay) {
                ArrowDisplay.NUMBER -> amount.toString()
                ArrowDisplay.PERCENTAGE -> "${percentage.toInt()}%"
            }
        }

        val element = CustomScoreboardRenderer.formatNumberDisplayDisplay(type, amountLine, color)
        return if (!hasCookieActive()) element else element.withActions {
            hover = listOf("§7Click to open the quiver")
            command = "/quiver"
        }
    }

    override fun showWhen() = McPlayer.inventory.any { it.item == Items.BOW }

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
