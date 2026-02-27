package me.owdding.customscoreboard.feature.customscoreboard

import com.teamresourceful.resourcefullib.common.color.ConstantColors
import earth.terrarium.olympus.client.components.Widgets
import earth.terrarium.olympus.client.components.compound.LayoutWidget
import earth.terrarium.olympus.client.components.string.TextWidget
import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.config.categories.CustomizationConfig
import me.owdding.customscoreboard.mixins.accessor.TextWidgetAccessor
import me.owdding.customscoreboard.utils.TextUtils.toComponent
import me.owdding.lib.builder.LayoutFactory
import me.owdding.lib.displays.Alignment
import me.owdding.lib.displays.Display
import me.owdding.lib.displays.DisplayWidget
import me.owdding.lib.layouts.Scalable
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.network.chat.Component
import net.minecraft.util.Util
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.translated
import tech.thatgravyboat.skyblockapi.utils.text.Text
import java.time.Duration
import java.time.temporal.ChronoUnit

data class ScoreboardLine(
    private val layout: AbstractWidget,
    val alignment: Alignment = DEFAULT_ALIGNMENT,
    val isBlank: Boolean = false,
    val actions: LineActions = LineActions(),
) {
    constructor(component: Component, alignment: Alignment = DEFAULT_ALIGNMENT, isBlank: Boolean = false) : this(component.asTextWidget(), alignment, isBlank)
    constructor(string: String, alignment: Alignment = DEFAULT_ALIGNMENT, isBlank: Boolean = false) : this(string.asTextWidget(), alignment, isBlank)

    val component: Component = (layout as? TextWidgetAccessor)?.text ?: "fail".toComponent()

    val widget: AbstractWidget by lazy {
        Widgets.button { button ->
            button.setSize(layout.width, layout.height)
            button.withTexture(null)
            button.withRenderer { graphics, ctx, ticks ->
                graphics.translated(button.x, button.y) {
                    layout.render(graphics, ctx.mouseX, ctx.mouseY, ticks)
                }
            }

            actions.hover?.let {
                button.withTooltip(Text.multiline(it))
                button.setTooltipDelay(Duration.of(-1, ChronoUnit.SECONDS))
            }

            button.withCallback {
                actions.command?.let { McClient.sendCommand(it.removePrefix("/")) }
                actions.link?.let { Util.getPlatform().openUri(it) }
                actions.click?.invoke()
            }
        }
    }

    fun withActions(block: ActionBuilder.() -> Unit): ScoreboardLine = this.copy(actions = ActionBuilder().apply(block).build())

    fun applySettings(settings: LayoutSettings) {
        settings.alignHorizontally(
            when (alignment) {
                Alignment.START -> 0f
                Alignment.CENTER -> 0.5f
                Alignment.END -> 1f
            },
        )
    }

    companion object {
        private val DEFAULT_ALIGNMENT get() = CustomizationConfig.defaultTextAlignment

        fun String.align(): ScoreboardLine = this.toComponent().align()
        fun Component.align(): ScoreboardLine = ScoreboardLine(this.asTextWidget(), DEFAULT_ALIGNMENT)
        infix fun String.align(alignment: Alignment): ScoreboardLine = this.toComponent().align(alignment)
        infix fun Component.align(alignment: Alignment): ScoreboardLine = ScoreboardLine(this.asTextWidget(), alignment)

        fun String.withActions(block: ActionBuilder.() -> Unit): ScoreboardLine = this.align().withActions(block)
        fun Component.withActions(block: ActionBuilder.() -> Unit): ScoreboardLine = this.align().withActions(block)

        internal fun getElementsFromAny(element: Any?): List<ScoreboardLine> = when (element) {
            null -> listOf()
            is Collection<*> -> element.mapNotNull { it?.toScoreboardElement() }
            else -> listOfNotNull(element.toScoreboardElement())
        }

        private fun Any.toScoreboardElement(): ScoreboardLine? = when (this) {
            is String -> this.align()
            is Component -> this.align()
            is ScoreboardLine -> this
            is Layout -> ScoreboardLine(LayoutWidget(this))
            is AbstractWidget -> ScoreboardLine(this)
            is Display -> ScoreboardLine(DisplayWidget(this))
            is Pair<*, *> -> {
                val element = this.first?.toScoreboardElement()
                val actionBuilder = this.second as? ActionBuilder

                if (element != null && actionBuilder != null) element.copy(actions = actionBuilder.build())
                else element
            }

            else -> null
        }

        fun List<ScoreboardLine>.createColumn() = (LayoutFactory.vertical(spacing = CustomizationConfig.lineSpacing) {
            this@createColumn.forEach { line ->
                widget(line.widget, line::applySettings)
            }
        }).also { (it as Scalable).scale(CustomizationConfig.scale) }

        fun getVanillaLines() = buildList {
            McClient.scoreboardTitle?.let { add(ScoreboardLine(it, CustomizationConfig.titleAlignment)) }
            McClient.scoreboard.forEach { add(ScoreboardLine(it)) }
        }
    }
}

data class LineActions(
    val hover: List<String>? = null,
    val command: String? = null,
    val click: (() -> Unit)? = null,
    val link: String? = null,
) {
    fun isEmpty() = hover == null && command == null && click == null && link == null
}

class ActionBuilder {
    var hover: List<String>? = null
    var command: String? = null
    var click: (() -> Unit)? = null
    var link: String? = null

    fun build() = LineActions(hover, command, click, link)
}

private fun String.asTextWidget() = toComponent().asTextWidget()

private fun Component.asTextWidget(): TextWidget {
    val textWidget = Widgets.text(this) {
        it.withColor(ConstantColors.white)
    }
    if (MainConfig.textShadow) {
        textWidget.withShadow()
    }
    return textWidget
}
