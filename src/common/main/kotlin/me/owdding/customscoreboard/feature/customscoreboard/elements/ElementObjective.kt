package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.TextUtils.isBlank
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@AutoElement
object ElementObjective : Element() {
    override fun getDisplay() = objectiveLines.map {
        it.withActions {
            hover = listOf("§7Click to view quest details")
            command = "/quests"
        }
    }

    override val configLine = "Objective"


    private val objectiveTitleRegex = "(Objective|Quest).*".toRegex()
    private val footerRegex = "(?:www|alpha).hypixel.net".toRegex()

    private val objectiveLines = mutableListOf<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        objectiveLines.clear()
        var objective = false

        for (component in event.components) {
            if (objective) {
                if (component.isBlank()) break
                if (footerRegex.matches(component.stripped)) break
                objectiveLines.add(component)
            } else {
                if (objectiveTitleRegex.matches(component.stripped)) {
                    objective = true
                    objectiveLines.add(component)
                }
            }
        }

    }
}
