package me.owdding.customscoreboard.utils.rendering.alignment

import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedName

enum class TextAlignment {
    START,
    CENTER,
    END;

    private val formattedName = toFormattedName()
    override fun toString() = formattedName

    fun align(value: Int, length: Int): Int = when (this) {
        START -> 0
        CENTER -> (length - value) / 2
        END -> length - value
    }
}
