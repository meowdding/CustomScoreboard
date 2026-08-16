package me.owdding.customscoreboard.core

import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.compat.ModCompat
import me.owdding.customscoreboard.config.Config
import me.owdding.customscoreboard.config.category.BackgroundConfig
import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.config.category.ModCompatibilityConfig
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.createColumn
import me.owdding.customscoreboard.elements.Element
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.rendering.RenderUtils.drawRec
import me.owdding.customscoreboard.utils.rendering.RenderUtils.drawTexture
import me.owdding.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import me.owdding.customscoreboard.utils.rendering.alignment.VerticalAlignment
import me.owdding.ktmodules.Module
import me.owdding.lib.overlays.EditableProperty
import me.owdding.lib.overlays.Overlay
import me.owdding.lib.overlays.OverlayAlignment
import me.owdding.lib.overlays.Position
import me.owdding.lib.overlays.Rect
import me.owdding.lib.platform.screens.MouseButtonEvent
import me.owdding.lib.platform.screens.MouseButtonInfo
import me.owdding.lib.platform.screens.mouseClicked
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.location.IslandChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.render.HudElement
import tech.thatgravyboat.skyblockapi.api.events.render.RenderHudElementEvent
import tech.thatgravyboat.skyblockapi.api.events.render.RenderHudEvent
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenMouseClickEvent
import tech.thatgravyboat.skyblockapi.api.events.time.TickEvent
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McScreen
import tech.thatgravyboat.skyblockapi.utils.extentions.currentInstant
import tech.thatgravyboat.skyblockapi.utils.extentions.isInPast
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.asComponent
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@Module
object CustomScoreboardRenderer : Overlay {

    var lines: List<ScoreboardLine> = emptyList()
        private set
    private var display: LayoutElement? = null
    private var nextPlannedUpdate = Instant.DISTANT_FUTURE
    private var currentIslandElements = emptyList<Element>()
    var currentIslandEvents = emptyList<ScoreboardEventEntry>()
        private set

    override var bounds: Pair<Int, Int> = 0 to 0
    override val modId: String = CustomScoreboardMod.MOD_ID
    override val name = Text.translatable("customscoreboard")

    var currentX = 0
        private set
    var currentY = 0
        private set

    override val position: Position = object : Position {
        override var x: Int
            get() = if (CustomizationConfig.horizontalAlignment == HorizontalAlignment.FREE_MOVE) CustomizationConfig.position.x else currentX
            set(value) {
                CustomizationConfig.position.x = value
            }
        override var y: Int
            get() = if (CustomizationConfig.verticalAlignment == VerticalAlignment.FREE_MOVE) CustomizationConfig.position.y else currentY
            set(value) {
                CustomizationConfig.position.y = value
            }
        override var scale: Float
            get() = CustomizationConfig.position.scale
            set(value) {
                CustomizationConfig.position.scale = value
            }
        override var alignment: OverlayAlignment
            get() = CustomizationConfig.position.alignment
            set(value) {
                CustomizationConfig.position.alignment = value
            }

        override fun resetPosition() = CustomizationConfig.position.resetPosition()
    }

    override val alignedX: Float
        get() = currentX.toFloat()

    override val editBounds: Rect
        get() = Rect(currentX, currentY, bounds.first, bounds.second)

    override val enabled: Boolean get() = isEnabled() && !renderScoreboardOverhaul()
    override val properties: Collection<EditableProperty>
        get() = buildList {
            if (CustomizationConfig.horizontalAlignment == HorizontalAlignment.FREE_MOVE) add(EditableProperty.X)
            if (CustomizationConfig.verticalAlignment == VerticalAlignment.FREE_MOVE) add(EditableProperty.Y)
        }

    private val screenWidth get() = McClient.window.guiScaledWidth
    private val screenHeight get() = McClient.window.guiScaledHeight

    fun tryUpdate(force: Boolean = false) {
        if (!isEnabled()) return
        if (force || nextPlannedUpdate.isInPast()) {
            updateDisplay()
            nextPlannedUpdate = currentInstant() + 250.milliseconds
        }
    }

    @Subscription(event = [TickEvent::class])
    fun onTick() {
        tryUpdate(Config.updateEveryTick)
    }

    @Subscription(event = [TickEvent::class])
    fun onScoreboardUpdate() {
        tryUpdate(true)
    }

    @Subscription
    fun onMouseClick(event: ScreenMouseClickEvent.Post) {
        if (!isAllowedScreen()) return

        display?.visitWidgets {
            if (it.mouseClicked(MouseButtonEvent(event.x, event.y, MouseButtonInfo(event.button, 0)), false)) {
                return@visitWidgets
            }
        }
    }

    @Subscription
    fun onRender(event: RenderHudEvent) {
        if (!isEnabled()) return
        if (renderScoreboardOverhaul()) return
        if (McClient.options.keyPlayerList.isDown && Config.hideWhenTab) return
        if (McScreen.isOf<ChatScreen>() && Config.hideWhenChat) return
        val display = display ?: return
        val (mouseX, mouseY) = McClient.mouse

        updatePosition()
        renderBackground(event)

        display.apply {
            setPosition(currentX, currentY)
        }.visitWidgets { widget ->
            if (isAllowedScreen() && Config.actions) {
                widget.extractRenderState(event.graphics, mouseX.toInt(), mouseY.toInt(), 0f)
            } else {
                widget.extractRenderState(event.graphics, 0, 0, 0f)
            }
        }
    }

    fun isAllowedScreen() = when (McScreen.self) {
        is ChatScreen, null -> true
        else -> false
    }

    private fun renderBackground(event: RenderHudEvent) {
        if (!BackgroundConfig.enabled) return
        val padding = BackgroundConfig.padding
        val borderOffset = if (BackgroundConfig.borderEnabled) BackgroundConfig.borderSize else 0

        val x = currentX - padding - borderOffset
        val y = currentY - padding - borderOffset
        val width = bounds.first + padding * 2 + borderOffset * 2
        val height = bounds.second + padding * 2 + borderOffset * 2

        if (BackgroundConfig.blurEnabled/*? < 26.2 {*/ /*&& !BlurredBackground.vulkanInstalled*//*?}*/) {
            BlurredBackground.render(event.graphics, x, y, width, height, BackgroundConfig.radius)
        }

        if (BackgroundConfig.imageBackground) {
            event.graphics.drawTexture(
                x, y, width, height,
                CustomScoreboardBackground.getTexture(),
                alpha = BackgroundConfig.imageBackgroundTransparency / 100f,
            )
        }
        event.graphics.drawRec(x, y, width, height)
    }

    fun updateIslandCache() {
        currentIslandElements = CustomizationConfig.appearance.filter { it.showIsland() }
        currentIslandEvents = CustomizationConfig.events.filter { it.event.showIsland() }
    }

    fun updateDisplay() {
        if (!isEnabled()) return
        lines = createDisplay().hideLeadingAndTrailingSeparators().condenseConsecutiveSeparators()
        display = lines.takeUnless { it.isEmpty() }?.createColumn()
    }

    private fun createDisplay() = currentIslandElements.flatMap { it.getLines() }.takeIf { shouldUseCustomLines() } ?: ScoreboardLine.getVanillaLines()

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
            val borderSize = if (borderEnabled) this.borderSize else 0

            currentX = when (CustomizationConfig.horizontalAlignment) {
                HorizontalAlignment.LEFT -> padding + margin + borderSize
                HorizontalAlignment.CENTER -> (screenWidth - width) / 2
                HorizontalAlignment.RIGHT -> screenWidth - width - padding - margin - borderSize
                HorizontalAlignment.FREE_MOVE -> {
                    val configX = CustomizationConfig.position.x
                    if (configX < 0) screenWidth + configX else configX
                }
            }

            currentY = when (CustomizationConfig.verticalAlignment) {
                VerticalAlignment.TOP -> padding + margin + borderSize
                VerticalAlignment.CENTER -> (screenHeight - height) / 2
                VerticalAlignment.BOTTOM -> screenHeight - height - padding - margin - borderSize
                VerticalAlignment.FREE_MOVE -> {
                    val configY = CustomizationConfig.position.y
                    if (configY < 0) screenHeight + configY else configY
                }
            }

            bounds = width to height
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

    fun formatNumberDisplayDisplay(text: String, number: String, color: String) = when (LinesConfig.numberDisplayFormat) {
        NumberDisplayFormat.TEXT_COLOR_NUMBER -> "§f$text: $color$number"
        NumberDisplayFormat.COLOR_TEXT_NUMBER -> "$color$text: $number"
        NumberDisplayFormat.COLOR_NUMBER_TEXT -> "$color$number $text"
        NumberDisplayFormat.COLOR_NUMBER_RESET_TEXT -> "$color$number §f$text"
    }

    fun formatNumberDisplayDisplay(text: String, number: Component, color: Int): Component = formatNumberDisplayDisplay(text.asComponent(), number, color)

    fun formatNumberDisplayDisplay(text: Component, number: Component, color: Int): Component = when (LinesConfig.numberDisplayFormat) {
        NumberDisplayFormat.TEXT_COLOR_NUMBER -> Text.join(text, Text.of(": "), number.copy().withColor(color))
        NumberDisplayFormat.COLOR_TEXT_NUMBER -> Text.join(text, Text.of(": "), number).withColor(color)
        NumberDisplayFormat.COLOR_NUMBER_TEXT -> Text.join(number, Text.of(" "), text).withColor(color)
        NumberDisplayFormat.COLOR_NUMBER_RESET_TEXT -> Text.join(number.copy().withColor(color), Text.of(" "), text)
    }

    enum class NumberDisplayFormat(val config: String) {
        TEXT_COLOR_NUMBER("§fPurse: §6123"),
        COLOR_TEXT_NUMBER("§6Purse: 123"),
        COLOR_NUMBER_TEXT("§6123 Purse"),
        COLOR_NUMBER_RESET_TEXT("§6123 §fPurse"),
        ;

        override fun toString() = config
    }

    private fun isEnabled() = (LocationAPI.isOnSkyBlock || Config.outsideSkyBlock) && Config.enabled
    fun shouldUseCustomLines() = Config.customLines && LocationAPI.isOnSkyBlock
    private fun hideHypixelScoreboard() = isEnabled() && Config.hideHypixelScoreboard
    fun renderScoreboardOverhaul() =
        LocationAPI.isOnSkyBlock && Config.enabled && ModCompatibilityConfig.scoreboardOverhaul && ModCompat.isScoreboardOverhaulEnabled()
}
