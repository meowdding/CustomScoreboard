package me.owdding.customscoreboard.feature.customscoreboard.events

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.utils.TextUtils.anyMatch
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@Module
@AutoElement
object EventServerRestart : Event() {
    override fun getDisplay() = formattedLine

    override val configLine = "Server Restart"


    private var formattedLine: Component? = null

    private val restartRegex = ComponentRegex("Server closing.*")

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (restartRegex.regex().anyMatch(event.removed)) {
            formattedLine = null
        }

        restartRegex.anyMatch(event.addedComponents) {
            formattedLine = it.component
        }

    }
}
