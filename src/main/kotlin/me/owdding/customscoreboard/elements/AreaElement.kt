package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.TextUtils.trim
import me.owdding.lib.utils.strings.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.hypixel.ServerChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.anyMatch
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch
import kotlin.reflect.KMutableProperty0

@ScoreboardElement
object AreaElement : Element() {
    override val configLine = "Area"
    override val id = "AREA"
    override val group = ElementGroup.HEADER

    override fun getDisplay() = listOfNotNull(formattedLocation, formattedGardenPlot, formattedVisiting)

    override fun shouldIndent(): Boolean = LinesConfig.indentLocationTimeLines

    private val remote = RemoteStrings.resolve()
    private val locationComponentRegex by remote.componentRegex("\\s*[⏣ф\uE067\uE020] .+")
    private val gardenPlotComponentRegex by remote.componentRegex("\\s*Plot -.+")
    private val visitingComponentRegex by remote.componentRegex("\\s*✌ \\(\\d+/\\d+\\)")

    private var formattedLocation: Component? = null
    private var formattedGardenPlot: Component? = null
    private var formattedVisiting: Component? = null

    private fun ComponentRegex.matchAreaRegex(event: ScoreboardUpdateEvent, prop: KMutableProperty0<Component?>, transform: (Component) -> Component = { it }) {
        val added = anyMatch(event.addedComponents) {
            prop.set(transform(it.component))
        }
        // if the line hasn't been added in this event, and it has been removed, we set it to null
        if (!added && prop.get() != null && regex().anyMatch(event.removed)) {
            prop.set(null)
        }
    }

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        locationComponentRegex.matchAreaRegex(event, ::formattedLocation) { it.trim() }
        gardenPlotComponentRegex.matchAreaRegex(event, ::formattedGardenPlot)
        visitingComponentRegex.matchAreaRegex(event, ::formattedVisiting) { it.trim() }
    }

    override fun onServerChange(event: ServerChangeEvent) {
        formattedLocation = null
        formattedGardenPlot = null
        formattedVisiting = null
    }

    override val regexes = setOf(locationComponentRegex, gardenPlotComponentRegex, visitingComponentRegex)
}
