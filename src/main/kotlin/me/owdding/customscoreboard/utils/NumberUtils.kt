package me.owdding.customscoreboard.utils

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.lib.extensions.shorten
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString

enum class NumberFormatType(val format: String) {
    LONG("1.234.567"),
    SHORT("1.2m"),
    ;

    override fun toString() = format
}

object NumberUtils {

    fun Number.format() = when (MainConfig.numberFormat) {
        NumberFormatType.LONG -> toDouble().toFormattedString()
        NumberFormatType.SHORT -> shorten()
    }
}

