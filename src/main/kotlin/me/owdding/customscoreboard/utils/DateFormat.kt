package me.owdding.customscoreboard.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class DateFormat(pattern: String) {
    ISO_YYYYMMDD("yyyy-MM-dd"),
    YEAR_MONTH_DAY("yyyy/MM/dd"),
    US_SLASH_MMDDYYYY("MM/dd/yyyy"),
    US_DASH_MMDDYYYY("MM-dd-yyyy"),
    UK_DDMMYYYY("dd/MM/yyyy"),
    UK_DASH_DDMMYYYY("dd-MM-yyyy"),
    DAY_MONTH_YEAR("dd MMMM yyyy"),
    DAY_MONTH_YEAR_SHORT("dd MMM yyyy"),
    FULL_MONTH_DAY_YEAR("MMMM dd, yyyy"),
    SHORT_MONTH_DAY_YEAR("MMM dd, yyyy"),
    ;

    private val formatter = DateTimeFormatter.ofPattern(pattern)

    override fun toString(): String = LocalDate.now().format(formatter)
}
