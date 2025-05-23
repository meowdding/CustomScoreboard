package me.owdding.customscoreboard.feature.customscoreboard

import me.owdding.customscoreboard.utils.TextUtils.toComponent
import me.owdding.customscoreboard.utils.rendering.AlignedText
import me.owdding.lib.displays.Alignment
import net.minecraft.network.chat.Component

data class ScoreboardLine(
    val display: Component,
    val alignment: Alignment = DEFAULT_ALIGNMENT,
) {

    fun toAlignedText(): AlignedText = display to alignment

    companion object {
        private val DEFAULT_ALIGNMENT get() = Alignment.START//displayConfig.textAlignment

        fun String.align(): ScoreboardLine = ScoreboardLine(this.toComponent(), DEFAULT_ALIGNMENT)

        fun Component.align(): ScoreboardLine = ScoreboardLine(this, DEFAULT_ALIGNMENT)

        infix fun String.align(alignment: Alignment): ScoreboardLine = ScoreboardLine(this.toComponent(), alignment)

        infix fun Component.align(alignment: Alignment): ScoreboardLine = ScoreboardLine(this, alignment)

        internal fun getElementsFromAny(element: Any?): List<ScoreboardLine> = when (element) {
            null -> listOf()
            is Component -> listOfNotNull(element.toScoreboardElement())
            is List<*> -> element.mapNotNull { it?.toScoreboardElement() }
            else -> listOfNotNull(element.toScoreboardElement())
        }

        private fun Any.toScoreboardElement(): ScoreboardLine? = when (this) {
            is String -> this.toComponent().align()
            is ScoreboardLine -> this
            is Component -> this.align()
            else -> null
        }
    }
}
