package gay.j10a1n15.customscoreboard.feature.customscoreboard

import gay.j10a1n15.customscoreboard.config.MainConfig
import gay.j10a1n15.customscoreboard.config.categories.BackgroundConfig
import gay.j10a1n15.customscoreboard.config.categories.LinesConfig
import gay.j10a1n15.customscoreboard.utils.TextUtils.isBlank
import gay.j10a1n15.customscoreboard.utils.rendering.AlignedText
import gay.j10a1n15.customscoreboard.utils.rendering.RenderUtils.drawAlignedTexts
import gay.j10a1n15.customscoreboard.utils.rendering.RenderUtils.drawRec
import gay.j10a1n15.customscoreboard.utils.rendering.RenderUtils.drawTexture
import gay.j10a1n15.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import gay.j10a1n15.customscoreboard.utils.rendering.alignment.VerticalAlignment
import me.owdding.ktmodules.Module
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

    private var display: List<AlignedText>? = null
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
        event.graphics.drawAlignedTexts(display, position.first, position.second, MainConfig.textShadow)
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

    private fun createDisplay() = currentIslandElements.flatMap { it.element.getAlignedText() }

    private fun List<AlignedText>.hideLeadingAndTrailingSeparators() =
        if (LinesConfig.hideSeparatorsAtStartEnd) this.dropLastWhile { it.first.isBlank() }.dropWhile { it.first.isBlank() } else this

    private fun List<AlignedText>.condenseConsecutiveSeparators() =
        if (!LinesConfig.condenseConsecutiveSeparators) this
        else
            fold(mutableListOf<AlignedText>() to false) { (acc, lastWasSeparator), line ->
                if (line.first.isBlank()) {
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
            val width = display?.let { it.maxOf { McFont.width(it.first) } } ?: 0
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
