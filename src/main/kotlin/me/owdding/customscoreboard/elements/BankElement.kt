package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.config.storage.CoopBankStorage
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.core.NumberTrackingElement
import me.owdding.customscoreboard.core.ScoreboardLine.Companion.withActions
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.ktmodules.Module
import me.owdding.lib.extensions.shorten
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.InventoryTitle
import tech.thatgravyboat.skyblockapi.api.events.screen.InventoryChangeEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.currency.CurrencyAPI
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.cleanName
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

@Module
@ScoreboardElement
object BankElement : NumberTrackingElement(TextColor.GOLD) {

    override fun format(number: Number): String {
        return if (LinesConfig.bankAlwaysCompact) number.shorten()
        else super.format(number)
    }

    fun line() = if (ProfileAPI.coop && CoopBankStorage.getCurrentProfile()) {
        when (LinesConfig.coopBankLayout) {
            CoopBankLayout.PERSONAL_COOP -> "${format(CurrencyAPI.personalBank)}§7/§6${format(CurrencyAPI.coopBank)}"
            CoopBankLayout.COOP_PERSONAL -> "${format(CurrencyAPI.coopBank)}§7/§6${format(CurrencyAPI.personalBank)}"
            CoopBankLayout.COMBINED -> format(CurrencyAPI.personalBank + CurrencyAPI.coopBank)
        }
    } else format(CurrencyAPI.coopBank)


    override fun getDisplay(): Any {
        checkDifference(CurrencyAPI.coopBank)
        val line = Text.join(line(), temporaryChangeDisplay)

        val element = CustomScoreboardRenderer.formatNumberDisplayDisplay("Bank", line, numberColor)
        return if (!EffectsAPI.isBoosterCookieActive) element else element.withActions {
            hover = listOf("§7Click to open the bank")
            command = "/bank"
        }
    }

    override fun showWhen() = !(LinesConfig.hidePurseInDungeons && SkyBlockIsland.THE_CATACOMBS.inIsland())
    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)
    override fun isLineActive() = CurrencyAPI.coopBank > 0 || CurrencyAPI.personalBank > 0

    override val configLine = "Bank"
    override val id = "BANK"
    override val configLineHover = listOf("Cannot be accurate enough,", "so it uses whats in the tablist")

    enum class CoopBankLayout(val display: String) {
        PERSONAL_COOP("Personal/Coop"),
        COOP_PERSONAL("Coop/Personal"),
        COMBINED("Combined Value");

        override fun toString() = display
    }

    @Subscription
    @InventoryTitle("Bank")
    fun onContainer(event: InventoryChangeEvent) {
        if (event.slot.index != 15) return
        val state = event.item.cleanName == "Personal Bank Account"
        CustomScoreboardMod.info("Determined Profile as actually coop: $state")
        CoopBankStorage.setCurrentProfile(state)
    }
}
