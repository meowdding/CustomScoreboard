package gay.j10a1n15.customscoreboard.utils.rendering.alignment

import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedName

enum class VerticalAlignment {
    TOP,
    CENTER,
    BOTTOM;

    private val formattedName = toFormattedName()
    override fun toString() = formattedName
}
