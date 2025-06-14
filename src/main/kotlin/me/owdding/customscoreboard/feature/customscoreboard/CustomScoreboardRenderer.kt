package me.owdding.customscoreboard.feature.customscoreboard

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.createColumn
import me.owdding.customscoreboard.generated.ScoreboardEntry
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.rendering.RenderUtils.drawRec
import me.owdding.customscoreboard.utils.rendering.RenderUtils.drawTexture
import me.owdding.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import me.owdding.customscoreboard.utils.rendering.alignment.VerticalAlignment
import me.owdding.ktmodules.Module
import me.owdding.lib.builder.LayoutBuilder.Companion.setPos
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.TimePassed
import tech.thatgravyboat.skyblockapi.api.events.location.IslandChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.render.HudElement
import tech.thatgravyboat.skyblockapi.api.events.render.RenderHudElementEvent
import tech.thatgravyboat.skyblockapi.api.events.render.RenderHudEvent
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenMouseClickEvent
import tech.thatgravyboat.skyblockapi.api.events.time.TickEvent
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.helpers.McClient

@Module
object CustomScoreboardRenderer {

    private var display: Layout? = null
    private var currentIslandElements = emptyList<ScoreboardEntry>()
    var currentIslandEvents = emptyList<ScoreboardEventEntry>()
        private set

    private var position: Pair<Int, Int> = 0 to 0
    private var dimensions: Pair<Int, Int> = 0 to 0

    private val screenWidth get() = McClient.window.guiScaledWidth
    private val screenHeight get() = McClient.window.guiScaledHeight


    @Subscription(event = [TickEvent::class])
    @TimePassed("10t")
    fun onTick() {
        if (!isEnabled()) return

        updateDisplay()
    }

    @Subscription
    fun onMouseClick(event: ScreenMouseClickEvent.Post) {
        if (!isAllowedScreen()) return

        display?.visitWidgets {
            if (it.mouseClicked(event.x, event.y, event.button)) {
                return@visitWidgets
            }
        }
    }

    @Subscription()
    fun onRender(event: RenderHudEvent) {
        if (!isEnabled()) return
        val display = display ?: return
        val (mouseX, mouseY) = McClient.mouse

        updatePosition()
        renderBackground(event)
        if (isAllowedScreen()) {
            display.setPos(position.first, position.second).visitWidgets { it.render(event.graphics, mouseX.toInt(), mouseY.toInt(), 0f) }
        } else {
            display.setPos(position.first, position.second).visitWidgets { it.render(event.graphics, 0, 0, 0f) }
        }
    }

    fun isAllowedScreen() = when (McClient.self.screen) {
        is ChatScreen, is ContainerScreen, is InventoryScreen, null -> true
        else -> false
    }


    private fun renderBackground(event: RenderHudEvent) {
        if (!BackgroundConfig.enabled) return
        val padding = BackgroundConfig.padding

        val x = position.first - padding
        val y = position.second - padding
        val width = dimensions.first + padding * 2
        val height = dimensions.second + padding * 2

        if (BackgroundConfig.imageBackground) {
            event.graphics.drawTexture(
                x, y, width, height,
                CustomScoreboardBackground.getTexture(),
                radius = BackgroundConfig.radius,
                alpha = BackgroundConfig.imageBackgroundTransparency / 100f,
            )
        } else {
            event.graphics.drawRec(
                x, y, width, height,
                BackgroundConfig.backgroundColor,
                radius = BackgroundConfig.radius,
            )
        }
    }

    fun updateIslandCache() {
        currentIslandElements = MainConfig.appearance.filter { it.element.showIsland() }
        currentIslandEvents = MainConfig.events.filter { it.event.showIsland() }
    }

    private fun updateDisplay() {
        if (!isEnabled()) return
        display = createDisplay().hideLeadingAndTrailingSeparators().condenseConsecutiveSeparators().takeUnless { it.isEmpty() }?.createColumn()
    }

    private fun createDisplay() = currentIslandElements.flatMap { it.element.getLines() }.takeIf { shouldUseCustomLines() } ?: ScoreboardLine.getVanillaLines()

    private fun List<ScoreboardLine>.hideLeadingAndTrailingSeparators() =
        if (LinesConfig.hideSeparatorsAtStartEnd) this.dropLastWhile { it.isBlank }.dropWhile { it.isBlank } else this

    private fun List<ScoreboardLine>.condenseConsecutiveSeparators() =
        if (!LinesConfig.condenseConsecutiveSeparators) this
        else
            fold(mutableListOf<ScoreboardLine>() to false) { (acc, lastWasSeparator), line ->
                if (line.isBlank) {
                    if (!lastWasSeparator) {
                        acc.add(line)
                    }
                    acc to true
                } else {
                    acc.add(line)
                    acc to false
                }
            }.first

    private fun updatePosition() {
        with(BackgroundConfig) {
            val width = display?.width ?: 0
            val height = display?.height ?: 0

            val newX = when (MainConfig.horizontalAlignment) {
                HorizontalAlignment.LEFT -> padding + margin
                HorizontalAlignment.CENTER -> (screenWidth - width) / 2
                HorizontalAlignment.RIGHT -> screenWidth - width - padding - margin
            }
            val newY = when (MainConfig.verticalAlignment) {
                VerticalAlignment.TOP -> padding + margin
                VerticalAlignment.CENTER -> (screenHeight - height) / 2
                VerticalAlignment.BOTTOM -> screenHeight - height - padding - margin
            }
            position = newX to newY
            dimensions = width to height
        }
    }


    @Subscription
    fun onRenderHudElement(event: RenderHudElementEvent) {
        if (event.element == HudElement.SCOREBOARD && hideHypixelScoreboard()) {
            event.cancel()
        }
    }

    @Subscription(event = [IslandChangeEvent::class])
    fun onIslandChange() {
        updateIslandCache()
    }

    fun formatNumberDisplayDisplay(text: String, number: String, color: String) = when (MainConfig.numberDisplayFormat) {
        NumberDisplayFormat.TEXT_COLOR_NUMBER -> "§f$text: $color$number"
        NumberDisplayFormat.COLOR_TEXT_NUMBER -> "$color$text: $number"
        NumberDisplayFormat.COLOR_NUMBER_TEXT -> "$color$number $text"
        NumberDisplayFormat.COLOR_NUMBER_RESET_TEXT -> "$color$number §f$text"
    }

    enum class NumberDisplayFormat(val config: String) {
        TEXT_COLOR_NUMBER("§fPurse: §6123"),
        COLOR_TEXT_NUMBER("§6Purse: 123"),
        COLOR_NUMBER_TEXT("§6123 Purse"),
        COLOR_NUMBER_RESET_TEXT("§6123 §fPurse"),
        ;

        override fun toString() = config
    }

    private fun isEnabled() = (LocationAPI.isOnSkyBlock || MainConfig.outsideSkyBlock) && MainConfig.enabled
    private fun shouldUseCustomLines() = MainConfig.customLines && !LocationAPI.isOnSkyBlock
    private fun hideHypixelScoreboard() = isEnabled() && MainConfig.hideHypixelScoreboard

}
