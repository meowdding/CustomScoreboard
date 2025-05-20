package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@Module
@AutoElement
object ElementSlayer : Element() {
    override fun getDisplay() = formattedLines

    override val configLine = "Slayer"


    private val slayerQuestRegex = "Slayer Quest".toRegex()

    private val formattedLines = emptyList<Component>().toMutableList()

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines.clear()
        var title = false

        for (component in event.components) {
            if (title) {
                if (formattedLines.size == 3) break
                formattedLines.add(component)
            } else {
                if (slayerQuestRegex.matches(component.stripped)) {
                    title = true
                    formattedLines.add(component)
                }
            }
        }

    }
}
