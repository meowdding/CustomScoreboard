package me.owdding.customscoreboard.utils.rendering.alignment

import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedName

enum class HorizontalAlignment {
    LEFT,
    CENTER,
    RIGHT,
    FREE_MOVE;

    private val formattedName = toFormattedName()
    override fun toString() = formattedName
}
