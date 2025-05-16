package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.Utils.getColoredName
import me.owdding.lib.extensions.ordinal
import tech.thatgravyboat.skyblockapi.api.datetime.DateTimeAPI

object ElementDate : Element() {
    override fun getDisplay() =
        DateTimeAPI.season?.run { if (LinesConfig.coloredMonth) getColoredName() else toString() } + " " + DateTimeAPI.day.let { "$it" + it.ordinal() }

    override fun showWhen() = DateTimeAPI.season != null

    override val configLine = "Date"
}
