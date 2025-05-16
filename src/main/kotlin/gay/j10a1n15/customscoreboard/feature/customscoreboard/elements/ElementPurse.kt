package gay.j10a1n15.customscoreboard.feature.customscoreboard.elements

import gay.j10a1n15.customscoreboard.config.categories.LinesConfig
import gay.j10a1n15.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import gay.j10a1n15.customscoreboard.feature.customscoreboard.NumberTrackingElement
import gay.j10a1n15.customscoreboard.utils.NumberUtils.format
import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.CurrencyAPI

@Module
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

    private val piggyRegex = "Piggy: .*".toRegex()

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        isPiggy = event.new.any { it.matches(piggyRegex) }
    }
}
