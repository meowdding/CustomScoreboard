package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.TextUtils.trim
import me.owdding.customscoreboard.utils.Utils.sublistFromFirst
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.OnlyWidget
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidgetChangeEvent
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

@Module
@ScoreboardElement
object ElementSlayer : Element() {
    override fun getDisplay() = formattedLines

    override val configLine = "Slayer"
    override val id = "SLAYER"


    private val slayerQuestRegex = ComponentRegex("Slayer Quest")

    private var formattedLines = emptyList<Component>()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        formattedLines = event.components.sublistFromFirst(3, slayerQuestRegex::matches)
    }

    @Subscription
    @OnlyWidget(TabWidget.SLAYER)
    fun onTabWidget(event: TabWidgetChangeEvent) {
        if (formattedLines.isNotEmpty()) return
        formattedLines = event.newComponents.sublistFromFirst(3, slayerQuestRegex::matches).map { it.trim() }
    }
}
