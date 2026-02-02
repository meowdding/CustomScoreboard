package me.owdding.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.feature.SkyHanniOption.shMapper
import me.owdding.customscoreboard.feature.SkyHanniOption.shPath
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBank
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMayor
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPowder
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementQuiver
import me.owdding.customscoreboard.utils.DateFormat
import me.owdding.customscoreboard.utils.NumberFormatType
import tech.thatgravyboat.skyblockapi.utils.extentions.valueOfOrNull

object LinesConfig : CategoryKt("line_modification") {
    override val name = Literal("Line Modification")
    private val translationPath = "customscoreboard.config.line_modification"

    init {
        separator { this.title = "$translationPath.sections.time" }
    }

    val dateFormat by enum(DateFormat.US_SLASH_MMDDYYYY) {
        this.translation = "$translationPath.date_format"
        this.shPath = "display.dateFormat"
        this.shMapper = { valueOfOrNull<DateFormat>(it.asString) ?: DateFormat.US_SLASH_MMDDYYYY }
    }

    val time24hFormat by boolean(false) {
        this.translation = "$translationPath.time_24h_format"
        this.shPath = "display.skyblockTime24hFormat"
    }

    val smoothTime by boolean(true) {
        this.translation = "$translationPath.smooth_time"
        this.shPath = "display.skyblockTimeExactMinutes"
    }

    val coloredMonth by boolean("colored_month", true) {
        this.translation = "$translationPath.colored_month"
    }

    init {
        separator { this.title = "$translationPath.sections.economy" }
    }

    val numberFormat by enum("number_format", NumberFormatType.LONG) {
        this.translation = "$translationPath.number_format"
        this.shPath = "display.numberFormat"
        this.shMapper = { valueOfOrNull<NumberFormatType>(it.asString) ?: NumberFormatType.LONG }
    }

    val numberDisplayFormat by enum("number_display_format", CustomScoreboardRenderer.NumberDisplayFormat.TEXT_COLOR_NUMBER) {
        this.translation = "$translationPath.number_display_format"
        this.shPath = "display.numberDisplayFormat"
    }

    val showCurrencyGain by boolean(true) {
        this.translation = "$translationPath.show_currency_gain"
        this.shPath = "display.showNumberDifference"
    }

    val showBitsAvailable by boolean("bits_available", true) {
        this.translation = "$translationPath.bits_available"
        this.shPath = "display.showUnclaimedBits"
    }

    val bankAlwaysCompact by boolean(false) {
        this.translation = "$translationPath.bank_always_compact"
    }

    val coopBankLayout by enum(ElementBank.CoopBankLayout.PERSONAL_COOP) {
        this.translation = "$translationPath.coop_bank_layout"
    }

    val showPiggy by boolean("piggy", false) {
        this.translation = "$translationPath.piggy"
    }

    val hidePurseInDungeons by boolean(false) {
        this.translation = "$translationPath.hide_purse_dungeons"
    }

    init {
        separator { this.title = "$translationPath.sections.progression" }
    }

    val slayerLevel by boolean(true) {
        this.translation = "$translationPath.slayer_level"
    }

    val magicalPower by boolean(true) {
        this.translation = "$translationPath.magical_power"
        this.shPath = "display.maxwell.showMagicalPower"
    }

    val colorArrowAmount by boolean("color_arrow", true) {
        this.translation = "$translationPath.color_arrow"
        this.shPath = "display.arrow.colorArrowAmount"
    }

    val arrowDisplay by enum("arrow_display", ElementQuiver.ArrowDisplay.NUMBER) {
        this.translation = "$translationPath.arrow_display"
        this.shPath = "display.arrow.arrowDisplay"
        this.shMapper = { valueOfOrNull<ElementQuiver.ArrowDisplay>(it.asString) ?: ElementQuiver.ArrowDisplay.NUMBER }
    }

    val powderDisplay by enum(ElementPowder.PowderDisplay.CURRENT) {
        this.translation = "$translationPath.powder_display"
        this.shPath = "display.powderDisplay"
        this.shMapper = {
            if (it.asString == "AVAILABLE") ElementPowder.PowderDisplay.CURRENT
            else valueOfOrNull<ElementPowder.PowderDisplay>(it.asString) ?: ElementPowder.PowderDisplay.CURRENT
        }
    }

    val showHypixelPowder by boolean("hypixel_powder", true) {
        this.translation = "$translationPath.hypixel_powder"
    }

    init {
        separator { this.title = "$translationPath.sections.social" }
    }

    val showProfileName by boolean("profile_name", false) {
        this.translation = "$translationPath.profile_name"
        this.shPath = "display.showProfileName"
    }

    val showPartyEverywhere by boolean("party_everywhere", true) {
        this.translation = "$translationPath.party_everywhere"
        this.shPath = "display.party.showPartyEverywhere"
    }

    val maxPartyMembers by int("max_party", 5) {
        this.translation = "$translationPath.max_party"
        this.slider = true
        this.range = 1..10
        this.shPath = "display.party.maxPartyList"
    }

    val showPartyLeader by boolean("party_leader", true) {
        this.translation = "$translationPath.party_leader"
        this.shPath = "display.party.showPartyLeader"
    }

    init {
        separator { this.title = "$translationPath.sections.pets" }
    }

    val petPrefix by boolean(true) {
        this.translation = "$translationPath.pet_prefix"
    }

    val showPetMax by boolean("pet_max", true) {
        this.translation = "$translationPath.pet_max"
    }

    init {
        separator { this.title = "$translationPath.sections.mayor" }
    }

    val showMayorTime by boolean("mayor_time", true) {
        this.translation = "$translationPath.mayor_time"
        this.shPath = "display.mayor.showTimeTillNextMayor"
    }

    val mayorPerksDisplay by enum(ElementMayor.PerkDisplay.ALL) {
        this.translation = "$translationPath.mayor_perks"
        this.shMapper = { if (it.asBoolean) ElementMayor.PerkDisplay.ALL else ElementMayor.PerkDisplay.OFF }
    }

    val ministerDisplay by enum(ElementMayor.MinisterDisplay.FULL) {
        this.translation = "$translationPath.mayor_minister"
        this.shPath = "display.mayor.showExtraMayor"
        this.shMapper = { if (it.asBoolean) ElementMayor.MinisterDisplay.FULL else ElementMayor.MinisterDisplay.OFF }
    }

    val showJerryInMinister by boolean(true) {
        this.translation = "$translationPath.jerry_minister"
        this.shPath = "display.showExtraMayor"
    }

    val showAllActiveEvents by boolean("all_events", true) {
        this.translation = "$translationPath.all_events"
        this.shPath = "display.events.showAllActiveEvents"
    }

    init {
        separator { this.title = "$translationPath.sections.cleanup" }
    }

    val showActiveOnly by boolean(false) {
        this.translation = "$translationPath.show_active_only"
        this.shPath = "informationFiltering.hideEmptyLines"
    }

    val condenseConsecutiveSeparators by boolean("consecutive_separator", true) {
        this.translation = "$translationPath.consecutive_separator"
        this.shPath = "informationFiltering.hideConsecutiveEmptyLines"
    }

    val hideSeparatorsAtStartEnd by boolean("separators_start_end", true) {
        this.translation = "$translationPath.separators_start_end"
        this.shPath = "informationFiltering.hideEmptyLinesAtTopAndBottom"
    }

}
