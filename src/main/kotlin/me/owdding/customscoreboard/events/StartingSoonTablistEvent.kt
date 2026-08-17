package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.core.TabWidgetHelper
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import me.owdding.customscoreboard.utils.TextUtils.removePrefix
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@AutoElement
object StartingSoonTablistEvent : Event() {
    private val startsInRegex by RemoteStrings.resolve().regex("\\s*Starts In: (?<time>.*)")

    override fun getDisplay(): List<Any>? {
        val lines = TabWidgetHelper.tabWidgetCache[TabWidget.EVENT] ?: return null
        if (lines.isEmpty()) return null

        val nameComponent = if (LinesConfig.showEventPrefix) lines.first() else lines.first().removePrefix("Event: ")

        val time = lines.firstNotNullOfOrNull { line ->
            startsInRegex.find(line.stripped)?.groups?.get("time")?.value
        } ?: return null

        return buildList {
            add(nameComponent) {
                hover = listOf("§7Click to open the calendar.")
                command = "/calendar"
            }
            add(
                Text.of(" Starts In: ") {
                    append(time, TextColor.YELLOW)
                },
            ) {
                hover = listOf("§7Click to open the calendar.")
                command = "/calendar"
            }
        }
    }

    override fun showWhen() = TabWidget.EVENT.isActive

    override val configLine = "Starting Soon Tablist Event"
    override val id = "STARTING_SOON_TABLIST"
}
