package gay.j10a1n15.customscoreboard.feature.customscoreboard.elements

import gay.j10a1n15.customscoreboard.feature.customscoreboard.ScoreboardLine
import gay.j10a1n15.customscoreboard.feature.customscoreboard.ScoreboardLine.Companion.getElementsFromAny

abstract class Element {
    /**
     * Must be specified as one of the following:
     * - `String`
     * - `List<String>`
     * - `ScoreboardLine` (`String align Alignment`)
     * - `List<ScoreboardLine>`
     * - `Component`
     *
     * `null` values will be treated as empty lines/lists.
     */
    protected abstract fun getDisplay(): Any?
    open fun showWhen(): Boolean = true
    abstract val configLine: String
    open val configLineHover = listOf<String>()

    open fun showIsland(): Boolean = true

    open fun getLines(): List<ScoreboardLine> = if (isVisible()) getElementsFromAny(getDisplay()) else listOf()
    fun getAlignedText() = getLines().map { it.toAlignedText() }

    private fun isVisible(): Boolean {
        //if (!informationFilteringConfig.hideIrrelevantLines) return true
        return showWhen()
    }
}
