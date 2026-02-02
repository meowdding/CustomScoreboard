package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.TextUtils.anyMatch
import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.OnlyWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidgetChangeEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.CurrencyAPI

@Module
@ScoreboardElement
object ElementSoulflow : NumberTrackingElement("§3") {

    override fun getDisplay(): String {
        checkDifference(CurrencyAPI.soulflow)
        val line = CurrencyAPI.soulflow.format() + temporaryChangeDisplay.orEmpty()

        return CustomScoreboardRenderer.formatNumberDisplayDisplay("Soulflow", line, numberColor)
    }

    override fun showWhen() = soulflowInTablist
    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)
    override fun isLineActive() = CurrencyAPI.soulflow > 0

    override val configLine = "Soulflow"
    override val id = "SOULFLOW"
    override val configLineHover = listOf("Requires the Soulflow option enabled in the Profile category in /tablist.", "Will not show if disabled.")

    private var soulflowInTablist = false
    private val soulflowRegex = " Soulflow: .*".toRegex()

    @Subscription
    @OnlyWidget(TabWidget.PROFILE)
    fun onTab(event: TabWidgetChangeEvent) {
        soulflowInTablist = soulflowRegex.anyMatch(event.new)
    }
}
