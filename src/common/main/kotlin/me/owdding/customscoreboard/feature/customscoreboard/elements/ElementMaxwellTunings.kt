package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.profile.maxwell.MaxwellAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

private const val MAX_TUNINGS_PER_LINE = 3

@ScoreboardElement
object ElementMaxwellTunings : Element() {
    override fun getDisplay(): Any {
        val tunings = MaxwellAPI.tunings
        if (tunings.isEmpty()) {
            return Text.of("Tunings: ") {
                append("None!", TextColor.RED)
            }
        }
        val tuningComponents = tunings.map { Text.of("${it.stat.icon}${it.value.toInt()}", color = it.stat.color) }
        return tuningComponents.chunked(MAX_TUNINGS_PER_LINE).mapIndexed { i, tunings ->
            Text.join(
                if (i == 0) "Tunings: " else null,
                Text.join(tunings, separator = SEPARATOR)
            )
        }

    }

    private val SEPARATOR = Text.of(", ", TextColor.GRAY)

    override val configLine: String get() = "Maxwell Tunings"
    override val id: String = "MAXWELL_TUNINGS"
}
