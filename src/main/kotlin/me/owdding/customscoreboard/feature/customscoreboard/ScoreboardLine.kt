package me.owdding.customscoreboard.feature.customscoreboard

import earth.terrarium.olympus.client.components.Widgets
import earth.terrarium.olympus.client.components.compound.LayoutWidget
import earth.terrarium.olympus.client.components.string.TextWidget
import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import me.owdding.customscoreboard.utils.TextUtils.toComponent
import me.owdding.lib.builder.LayoutFactory
import me.owdding.lib.displays.Alignment
import me.owdding.lib.displays.Display
import me.owdding.lib.displays.DisplayWidget
import net.minecraft.Util
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.translated
import tech.thatgravyboat.skyblockapi.utils.text.Text
import java.time.Duration
import java.time.temporal.ChronoUnit

data class ScoreboardLine(
    private val layout: AbstractWidget,
    val alignment: Alignment = DEFAULT_ALIGNMENT,
    val isBlank: Boolean = false,
) {
    constructor(
        string: String,
        alignment: Alignment = DEFAULT_ALIGNMENT,
        isBlank: Boolean = false,
    ) : this(
        string.asTextWidget(),
        alignment,
        isBlank,
    )

    private var actions: Map<Element.Actions, Any> = emptyMap()

    val widget by lazy {
        Widgets.button {
            it.setSize(layout.width, layout.height)
            it.withTexture(null)
            it.withRenderer { graphics, ctx, ticks ->
                graphics.translated(it.x, it.y) {
                    layout.render(graphics, ctx.mouseX, ctx.mouseY, ticks)
                }
            }

            if (Element.Actions.HOVER in actions) {
                it.withTooltip(Text.multiline(actions[Element.Actions.HOVER]))
                it.setTooltipDelay(Duration.of(-1, ChronoUnit.SECONDS))
            }

            it.withCallback {
                actions.forEach { (k, v) ->
                    when (k) {
                        Element.Actions.COMMAND -> McClient.self.connection?.sendCommand((v as String).removePrefix("/"))
                        Element.Actions.CLICK -> (v as (() -> Unit))()
                        Element.Actions.LINK -> Util.getPlatform().openUri(v as String)
                        else -> {}
                    }
                }
            }

        }
    }


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
        private val DEFAULT_ALIGNMENT get() = Alignment.START//displayConfig.textAlignment

        fun String.align(): ScoreboardLine = this.toComponent().align()

        fun Component.align(): ScoreboardLine = ScoreboardLine(this.asTextWidget(), DEFAULT_ALIGNMENT)

        infix fun String.align(alignment: Alignment): ScoreboardLine = this.toComponent().align(alignment)

        infix fun Component.align(alignment: Alignment): ScoreboardLine = ScoreboardLine(this.asTextWidget(), alignment)

        internal fun getElementsFromAny(element: Any?): List<ScoreboardLine> = when (element) {
            null -> listOf()
            is List<*> -> element.mapNotNull { it?.toScoreboardElement() }
            else -> listOfNotNull(element.toScoreboardElement())
        }

        private fun Any.toScoreboardElement(): ScoreboardLine? = when (this) {
            is String -> this.toComponent().align()
            is Component -> this.align()
            is ScoreboardLine -> this
            is Layout -> ScoreboardLine(LayoutWidget(this))
            is AbstractWidget -> ScoreboardLine(this)
            is Display -> ScoreboardLine(DisplayWidget(this))
            is Pair<*, *> -> {
                (this.second as? ActionBuilder)?.let { action ->
                    this.first!!.toScoreboardElement()?.apply { this.actions = action.toMap() }
                }
            }

            else -> null
        }

        fun List<ScoreboardLine>.createColumn() = LayoutFactory.vertical {
            this@createColumn.forEach { line ->
                widget(line.widget, line::applySettings)
            }
        }
    }
}

class ActionBuilder() {
    var hover: List<String>? = null
    var command: String? = null
    var click: (() -> Unit)? = null
    var link: String? = null

    fun toMap() = mapOf(
        Element.Actions.HOVER to hover,
        Element.Actions.COMMAND to command,
        Element.Actions.CLICK to click,
        Element.Actions.LINK to link,
    ).mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
}

private fun String.asTextWidget() = toComponent().asTextWidget()

private fun Component.asTextWidget(): TextWidget {
    val textWidget = Widgets.text(this)
    if (MainConfig.textShadow) {
        textWidget.withShadow()
    }
    return textWidget
}
