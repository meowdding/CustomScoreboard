package me.owdding.customscoreboard.feature.customscoreboard

import earth.terrarium.olympus.client.components.Widgets
import earth.terrarium.olympus.client.components.string.TextWidget
import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.utils.TextUtils.toComponent
import me.owdding.lib.builder.LayoutFactory
import me.owdding.lib.displays.Alignment
import me.owdding.lib.displays.Display
import me.owdding.lib.displays.DisplayWidget
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.network.chat.Component

data class ScoreboardLine(
    val layout: LayoutElement,
    val alignment: Alignment = DEFAULT_ALIGNMENT,
    val isBlank: Boolean = false,
) {
    fun applySettings(settings: LayoutSettings) {
        settings.alignHorizontally(
            when (alignment) {
                Alignment.START -> 0f
                Alignment.CENTER -> 0.5f
                Alignment.END -> 1f
            },
        )
    }

    constructor(
        string: String,
        alignment: Alignment = DEFAULT_ALIGNMENT,
        isBlank: Boolean = false,
    ) : this(
        string.textWidgetWithPotentialShadow(),
        alignment,
        isBlank,
    )

    companion object {
        private val DEFAULT_ALIGNMENT get() = Alignment.START//displayConfig.textAlignment

        fun String.align(): ScoreboardLine = this.toComponent().align()

        fun Component.align(): ScoreboardLine = ScoreboardLine(this.textWidgetWithPotentialShadow(), DEFAULT_ALIGNMENT)

        infix fun String.align(alignment: Alignment): ScoreboardLine = this.toComponent().align(alignment)

        infix fun Component.align(alignment: Alignment): ScoreboardLine = ScoreboardLine(this.textWidgetWithPotentialShadow(), alignment)

        internal fun getElementsFromAny(element: Any?): List<ScoreboardLine> = when (element) {
            null -> listOf()
            is List<*> -> element.mapNotNull { it?.toScoreboardElement() }
            else -> listOfNotNull(element.toScoreboardElement())
        }

        private fun Any.toScoreboardElement(): ScoreboardLine? = when (this) {
            is String -> this.toComponent().align()
            is Component -> this.align()
            is ScoreboardLine -> this
            is LayoutElement -> ScoreboardLine(this)
            is Display -> ScoreboardLine(DisplayWidget(this))
            else -> null
        }

        fun List<ScoreboardLine>.createColumn() = LayoutFactory.vertical {
            this@createColumn.forEach { line ->
                widget(line.layout, line::applySettings)
            }
        }
    }
}

private fun String.textWidgetWithPotentialShadow() = toComponent().textWidgetWithPotentialShadow()

private fun Component.textWidgetWithPotentialShadow(): TextWidget {
    val textWidget = Widgets.text(this)
    if (MainConfig.textShadow) {
        textWidget.withShadow()
    }
    return textWidget
}
