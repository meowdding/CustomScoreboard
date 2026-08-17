package me.owdding.customscoreboard.utils

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.lib.extensions.shorten
import tech.thatgravyboat.skyblockapi.utils.extentions.toFormattedString
import java.text.NumberFormat
import java.util.Locale

enum class NumberFormatType(val format: String) {
    LONG("1.234.567"),
    SHORT("1.2m"),
    ;

    override fun toString() = format
}

object NumberUtils {

    fun Number.formatLong() = if (LinesConfig.forcedLocale) NumberFormat.getNumberInstance(Locale.US).format(this)
    else toDouble().toFormattedString() // Formats to Locale.US if LinesConfig.forcedLocale

    fun Number.format() = when (LinesConfig.numberFormat) {
        NumberFormatType.LONG -> formatLong()
        NumberFormatType.SHORT -> shorten()
    }
}

