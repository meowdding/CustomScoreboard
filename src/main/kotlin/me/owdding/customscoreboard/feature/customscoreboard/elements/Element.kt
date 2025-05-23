package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine
import me.owdding.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.getElementsFromAny
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI
import tech.thatgravyboat.skyblockapi.api.events.hypixel.ServerChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent

abstract class Element {
    init {
        SkyBlockAPI.eventBus.register<ScoreboardUpdateEvent> { event -> onScoreboardUpdate(event) }
        SkyBlockAPI.eventBus.register<ServerChangeEvent> { event -> onServerChange(event) }
    }

    /**
     * Must be specified as one of the following:
     * - `String`
     * - `ScoreboardLine`
     * - `Component`
     * And any of the above can be a list of the above.
     *
     * `null` values will be treated as empty lines/lists.
     */
    protected abstract fun getDisplay(): Any?
    open fun showWhen(): Boolean = true
    abstract val configLine: String
    open val configLineHover = listOf<String>()

    open fun showIsland(): Boolean = true

    open fun getLines(): List<ScoreboardLine> = if (isVisible()) getElementsFromAny(getDisplay()) else listOf()

    private fun isVisible(): Boolean {
        //if (!informationFilteringConfig.hideIrrelevantLines) return true
        return showWhen()
    }

    open fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {}
    open fun onServerChange(event: ServerChangeEvent) {}
}
