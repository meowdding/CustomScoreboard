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
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.TimePassed
import tech.thatgravyboat.skyblockapi.api.events.location.IslandChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.render.HudElement
import tech.thatgravyboat.skyblockapi.api.events.render.RenderHudElementEvent
import tech.thatgravyboat.skyblockapi.api.events.render.RenderHudEvent
import tech.thatgravyboat.skyblockapi.api.events.time.TickEvent
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McFont

@Module
object CustomScoreboardRenderer {

    private var display: List<ScoreboardLine>? = null
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

    @Subscription()
    fun onRender(event: RenderHudEvent) {
        if (!isEnabled()) return
        val display = display ?: return
        if (display.isEmpty()) return

        updatePosition()
        renderBackground(event)
        display.createColumn().setPos(position.first, position.second).visitWidgets { it.render(event.graphics, 0, 0, 0f) }
    }

    private fun renderBackground(event: RenderHudEvent) {
        if (!BackgroundConfig.enabled) return
        val padding = BackgroundConfig.padding

        if (BackgroundConfig.imageBackground) {
            event.graphics.drawTexture(
                position.first - padding, position.second - padding,
                dimensions.first + padding * 2, dimensions.second + padding * 2,
                CustomScoreboardBackground.getTexture(),
                radius = BackgroundConfig.radius,
                alpha = BackgroundConfig.imageBackgroundTransparency / 100f,
            )
        } else {
            event.graphics.drawRec(
                position.first - padding, position.second - padding,
                dimensions.first + padding * 2, dimensions.second + padding * 2,
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
        display = createDisplay().hideLeadingAndTrailingSeparators().condenseConsecutiveSeparators()
    }

    private fun createDisplay() = currentIslandElements.flatMap { it.element.getLines() }

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
            val width = display?.let { it.maxOf { it.layout.width } } ?: 0
            val height = display?.let { it.size * McFont.self.lineHeight } ?: 0

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

    private fun isEnabled() = LocationAPI.isOnSkyBlock && MainConfig.enabled
    private fun hideHypixelScoreboard() = isEnabled() && MainConfig.hideHypixelScoreboard

}
