package me.owdding.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.core.TabWidgetHelper
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@AutoElement
object ActiveTablistEvent : Event() {
    private val blockedEvents = listOf("Spooky Festival", "Carnival", "th SkyBlock Anniversary", "New Year Celebration")
    private val endsInRegex by RemoteStrings.resolve().regex("\\s*Ends In: (?<time>.*)")

    override fun getDisplay(): List<Any>? {
        val lines = TabWidgetHelper.tabWidgetCache[TabWidget.EVENT] ?: return null
        if (lines.isEmpty()) return null

        val nameComponent = lines.first()
        val nameString = nameComponent.stripped.trim()

        if (blockedEvents.any { nameString.contains(it) }) return null

        val time = lines.firstNotNullOfOrNull { line ->
            endsInRegex.find(line.stripped)?.groups?.get("time")?.value
        } ?: return null

        return buildList {
            add(nameComponent) {
                hover = listOf("§7Click to open the calendar.")
                command = "/calendar"
            }
            add(
                Text.of(" Ends In: ") {
                    append(time, TextColor.YELLOW)
                },
            ) {
                hover = listOf("§7Click to open the calendar.")
                command = "/calendar"
            }
        }
    }

    override fun showWhen() = TabWidget.EVENT.isActive

    override val configLine = "Active Tablist Event"
    override val id = "ACTIVE_TABLIST"
}
