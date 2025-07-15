package me.owdding.customscoreboard.utils.rendering.alignment

import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedName

enum class HorizontalAlignment {
    LEFT,
    CENTER,
    RIGHT;

    private val formattedName = toFormattedName()
    override fun toString() = formattedName
}
