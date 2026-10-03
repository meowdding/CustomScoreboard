package me.owdding.customscoreboard.config.category

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.compat.SkyHanniOption.shMapper
import me.owdding.customscoreboard.compat.SkyHanniOption.shPath
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.elements.BankElement
import me.owdding.customscoreboard.elements.MayorElement
import me.owdding.customscoreboard.elements.PowderElement
import me.owdding.customscoreboard.elements.QuiverElement
import me.owdding.customscoreboard.utils.DateFormat
import me.owdding.customscoreboard.utils.NumberFormatType
import tech.thatgravyboat.skyblockapi.utils.extentions.valueOfOrNull

object LinesConfig : CategoryKt("line_modification") {
    override val name = Literal("Line Modification")
    override val baseTranslation: String = "customscoreboard.config.line_modification"

    init {
        separator { this.title = "sections.time" }
    }

    val dateFormat by enum(DateFormat.US_SLASH_MMDDYYYY) {
        this.translation = "date_format"
        this.shPath = "display.dateFormat"
        this.shMapper = { valueOfOrNull<DateFormat>(it.asString) ?: DateFormat.US_SLASH_MMDDYYYY }
    }

    val dateInLobbyCode by boolean(true) {
        this.translation = "date_in_lobby_code"
        this.shPath = "display.dateInLobbyCode"
    }

    val time24hFormat by boolean(false) {
        this.translation = "time_24h_format"
        this.shPath = "display.skyblockTime24hFormat"
    }

    val includeMinutes by boolean(true) {
        this.translation = "include_minutes"
    }

    val smoothTime by boolean(true) {
        this.translation = "smooth_time"
        this.shPath = "display.skyblockTimeExactMinutes"
    }

    val coloredMonth by boolean("colored_month", false) {
        this.translation = "colored_month"
    }

    val indentLocationTimeLines by boolean(false) {
        this.translation = "indent_location_time"
    }

    init {
        separator { this.title = "sections.economy" }
    }

    val numberFormat by enum("number_format", NumberFormatType.LONG) {
        this.translation = "number_format"
        this.shPath = "display.numberFormat"
        this.shMapper = { valueOfOrNull<NumberFormatType>(it.asString) ?: NumberFormatType.LONG }
    }

    val numberDisplayFormat by enum("number_display_format", CustomScoreboardRenderer.NumberDisplayFormat.TEXT_COLOR_NUMBER) {
        this.translation = "number_display_format"
        this.shPath = "display.numberDisplayFormat"
        this.shMapper = {
            valueOfOrNull<CustomScoreboardRenderer.NumberDisplayFormat>(it.asString) ?: CustomScoreboardRenderer.NumberDisplayFormat.TEXT_COLOR_NUMBER
        }
    }

    val forcedLocale by boolean (false) {
        this.translation = "forced_number_format_locale"
    }

    val showCurrencyGain by boolean(true) {
        this.translation = "show_currency_gain"
        this.shPath = "display.showNumberDifference"
    }

    val showBitsAvailable by boolean("bits_available", true) {
        this.translation = "bits_available"
        this.shPath = "display.showUnclaimedBits"
    }

    val gemsAlwaysCompact by boolean(true) {
        this.translation = "gems_always_compact"
    }

    val bankAlwaysCompact by boolean(true) {
        this.translation = "bank_always_compact"
    }

    val coopBankLayout by enum(BankElement.CoopBankLayout.PERSONAL_COOP) {
        this.translation = "coop_bank_layout"
    }

    val showPiggy by boolean("piggy", false) {
        this.translation = "piggy"
    }

    val hidePurseInDungeons by boolean(false) {
        this.translation = "hide_purse_dungeons"
    }

    init {
        separator { this.title = "sections.progression" }
    }

    val slayerLevel by boolean(true) {
        this.translation = "slayer_level"
    }

    val magicalPower by boolean(true) {
        this.translation = "accessory_power"
        this.shPath = "display.maxwell.showMagicalPower"
        this.searchTerms += "magical"
    }

    val colorArrowAmount by boolean("color_arrow", true) {
        this.translation = "color_arrow"
        this.shPath = "display.arrow.colorArrowAmount"
    }

    val arrowDisplay by enum("arrow_display", QuiverElement.ArrowDisplay.NUMBER) {
        this.translation = "arrow_display"
        this.shPath = "display.arrow.arrowDisplay"
        this.shMapper = { valueOfOrNull<QuiverElement.ArrowDisplay>(it.asString) ?: QuiverElement.ArrowDisplay.NUMBER }
    }

    val powderDisplay by enum(PowderElement.PowderDisplay.CURRENT) {
        this.translation = "powder_display"
        this.shPath = "display.powderDisplay"
        this.shMapper = {
            if (it.asString == "AVAILABLE") PowderElement.PowderDisplay.CURRENT
            else valueOfOrNull<PowderElement.PowderDisplay>(it.asString) ?: PowderElement.PowderDisplay.CURRENT
        }
    }

    val showHypixelPowder by boolean("hypixel_powder", true) {
        this.translation = "hypixel_powder"
    }

    val addBetterTogetherTitle by boolean(false) {
        this.translation = "better_together_title"
        // This is a Fake Line added in sh and if youre coming from that youre used to it
        this.shPath = "enabled"
    }

    init {
        separator { this.title = "sections.social" }
    }

    val showProfileName by boolean("profile_name", false) {
        this.translation = "profile_name"
        this.shPath = "display.showProfileName"
    }

    val showPartyEverywhere by boolean("party_everywhere", true) {
        this.translation = "party_everywhere"
        this.shPath = "display.party.showPartyEverywhere"
    }

    val maxPartyMembers by int("max_party", 5) {
        this.translation = "max_party"
        this.slider = true
        this.range = 1..10
        this.shPath = "display.party.maxPartyList"
    }

    val showPartyLeader by boolean("party_leader", true) {
        this.translation = "party_leader"
        this.shPath = "display.party.showPartyLeader"
    }

    init {
        separator { this.title = "sections.pets" }
    }

    val petPrefix by boolean(true) {
        this.translation = "pet_prefix"
    }

    val showPetMax by boolean("pet_max", true) {
        this.translation = "pet_max"
    }

    init {
        separator { this.title = "sections.mayor" }
    }

    val showMayorTime by boolean("mayor_time", true) {
        this.translation = "mayor_time"
        this.shPath = "display.mayor.showTimeTillNextMayor"
    }

    val mayorPerksDisplay by enum(MayorElement.PerkDisplay.ALL) {
        this.translation = "mayor_perks"
        this.shPath = "display.mayor.showMayorPerks"
        this.shMapper = { if (it.asBoolean) MayorElement.PerkDisplay.ALL else MayorElement.PerkDisplay.OFF }
    }

    val showFoxyEvent by boolean(true) {
        this.translation = "foxy_event"
    }

    val ministerDisplay by enum(MayorElement.MinisterDisplay.FULL) {
        this.translation = "mayor_minister"
        this.shPath = "display.mayor.showExtraMayor"
        this.shMapper = { if (it.asBoolean) MayorElement.MinisterDisplay.FULL else MayorElement.MinisterDisplay.OFF }
    }

    val showJerryInMinister by boolean(true) {
        this.translation = "jerry_minister"
        this.shPath = "display.showExtraMayor"
    }

    val showAllActiveEvents by boolean("all_events", true) {
        this.translation = "all_events"
        this.shPath = "display.events.showAllActiveEvents"
    }

    val separatorBetweenEvents by boolean(false) {
        this.translation = "separator_between_events"
    }

    val showEventPrefix by boolean (true) {
        this.translation = "show_event_prefix"
    }

    val tablistEventsInDungeon by boolean(true) {
        this.translation = "tablist_event_dungeons"
    }

    init {
        separator { this.title = "sections.cleanup" }
    }

    val showActiveOnly by boolean(false) {
        this.translation = "show_active_only"
        this.shPath = "informationFiltering.hideEmptyLines"
    }

    val hideSlayerOutsideSlayerAreas by boolean(false) {
        this.translation = "slayer_outside_slayerareas"
        this.shPath = "informationFiltering.hideIrrelevantLines"
    }

    val condenseConsecutiveSeparators by boolean("consecutive_separator", true) {
        this.translation = "consecutive_separator"
        this.shPath = "informationFiltering.hideConsecutiveEmptyLines"
    }

    val hideSeparatorsAtStartEnd by boolean("separators_start_end", true) {
        this.translation = "separators_start_end"
        this.shPath = "informationFiltering.hideEmptyLinesAtTopAndBottom"
    }

}
