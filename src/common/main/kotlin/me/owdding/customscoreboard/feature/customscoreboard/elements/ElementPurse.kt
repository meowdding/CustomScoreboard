package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.NumberTrackingElement
import me.owdding.customscoreboard.utils.NumberUtils.format
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.CurrencyAPI

@ScoreboardElement
object ElementPurse : Element(), NumberTrackingElement {
    override var previousAmount: Long = -1
    override var temporaryChangeDisplay: String? = null
    override val numberColor = "§6"

    private var isPiggy = false

    override fun getDisplay(): String {
        checkDifference(CurrencyAPI.purse.toLong())
        val line = CurrencyAPI.purse.format() + temporaryChangeDisplay.orEmpty()
        return CustomScoreboardRenderer.formatNumberDisplayDisplay(
            if (isPiggy && LinesConfig.showPiggy) "Piggy" else "Purse",
            line,
            numberColor,
        )
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override val configLine = "Purse"
    override val id = "PURSE"

    private val piggyRegex = "Piggy: .*".toRegex()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        isPiggy = event.new.any { it.matches(piggyRegex) }
    }
}
