package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.utils.CommonRegexes
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.TextUtils.isBlank
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.contains
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@ScoreboardElement
object ElementObjective : Element() {
    override fun getDisplay() = objectiveLines.map {
        it.withActions {
            hover = listOf("§7Click to view quest details")
            command = "/quests"
        }
    }

    override val configLine = "Objective"
    override val id = "OBJECTIVE"


    private val objectiveTitleRegex = "^(?:Objective|Quest)".toRegex()

    private val objectiveLines = mutableListOf<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        objectiveLines.clear()
        var objective = false

        for (component in event.components) {
            if (objective) {
                if (component.isBlank()) break
                if (CommonRegexes.hypixelFooterRegex.matches(component)) break
                objectiveLines.add(component)
            } else {
                if (objectiveTitleRegex.contains(component.stripped)) {
                    objective = true
                    objectiveLines.add(component)
                }
            }
        }

    }
}
