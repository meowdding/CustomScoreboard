package me.owdding.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.feature.SkyHanniOption.shMapper
import me.owdding.customscoreboard.feature.SkyHanniOption.shPath
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBank
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMayor
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPowder
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementQuiver
import me.owdding.customscoreboard.utils.DateFormat
import tech.thatgravyboat.skyblockapi.utils.extentions.valueOfOrNull

object LinesConfig : CategoryKt("line_modification") {

    override val name = Literal("Line Modification")

    val dateFormat by enum(DateFormat.US_SLASH_MMDDYYYY) {
        this.translation = "customscoreboard.config.lines.date_format"
        this.shPath = "display.dateFormat"
        this.shMapper = { valueOfOrNull<DateFormat>(it.asString) ?: DateFormat.US_SLASH_MMDDYYYY }
    }

    val showBitsAvailable by boolean("bits_available", true) {
        this.translation = "customscoreboard.config.lines.bits_available"
        this.shPath = "display.showUnclaimedBits"
    }

    val coloredMonth by boolean("colored_month", true) {
        this.translation = "customscoreboard.config.lines.colored_month"
    }

    val showProfileName by boolean("profile_name", false) {
        this.translation = "customscoreboard.config.lines.profile_name"
        this.shPath = "display.showProfileName"
    }

    val showMayorTime by boolean("mayor_time", true) {
        this.translation = "customscoreboard.config.lines.mayor_time"
        this.shPath = "display.mayor.showTimeTillNextMayor"
    }

    val mayorPerksDisplay by enum(ElementMayor.PerkDisplay.ALL) {
        this.translation = "customscoreboard.config.lines.mayor_perks"
        this.shMapper = {
            if (it.asBoolean) ElementMayor.PerkDisplay.ALL
            else ElementMayor.PerkDisplay.OFF
        }
    }

    val ministerDisplay by enum(ElementMayor.MinisterDisplay.FULL) {
        this.translation = "customscoreboard.config.lines.mayor_minister"
        this.shPath = "display.mayor.showExtraMayor"
        this.shMapper = {
            if (it.asBoolean) ElementMayor.MinisterDisplay.FULL
            else ElementMayor.MinisterDisplay.OFF
        }
    }

    val showJerryInMinister by boolean(true) {
        this.translation = "customscoreboard.config.lines.jerry_minister"
        this.shPath = "display.showExtraMayor"
    }

    val showAllActiveEvents by boolean("all_events", true) {
        this.translation = "customscoreboard.config.lines.all_events"
        this.shPath = "display.events.showAllActiveEvents"
    }

    val useHypixelTitle by boolean("hypixel_title", true) {
        this.translation = "customscoreboard.config.lines.hypixel_title"
        this.shPath = "display.titleAndFooter.useCustomTitle"
        this.shMapper = { !it.asBoolean }
    }

    val showPartyEverywhere by boolean("party_everywhere", true) {
        this.translation = "customscoreboard.config.lines.party_everywhere"
        this.shPath = "display.party.showPartyEverywhere"
    }

    val maxPartyMembers by int("max_party", 5) {
        this.translation = "customscoreboard.config.lines.max_party"
        this.slider = true
        this.range = 1..10
        this.shPath = "display.party.maxPartyList"
    }

    val showPartyLeader by boolean("party_leader", true) {
        this.translation = "customscoreboard.config.lines.party_leader"
        this.shPath = "display.party.showPartyLeader"
    }

    val condenseConsecutiveSeparators by boolean("consecutive_separator", true) {
        this.translation = "customscoreboard.config.lines.consecutive_separator"
        this.shPath = "informationFiltering.hideConsecutiveEmptyLines"
    }

    val hideSeparatorsAtStartEnd by boolean("separators_start_end", true) {
        this.translation = "customscoreboard.config.lines.separators_start_end"
        this.shPath = "informationFiltering.hideEmptyLinesAtTopAndBottom"
    }

    val colorArrowAmount by boolean("color_arrow", true) {
        this.translation = "customscoreboard.config.lines.color_arrow"
        this.shPath = "display.arrow.colorArrowAmount"
    }

    val arrowDisplay by enum("arrow_display", ElementQuiver.ArrowDisplay.NUMBER) {
        this.translation = "customscoreboard.config.lines.arrow_display"
        this.shPath = "display.arrow.arrowDisplay"
        this.shMapper = { valueOfOrNull<ElementQuiver.ArrowDisplay>(it.asString) ?: ElementQuiver.ArrowDisplay.NUMBER }
    }

    val showPiggy by boolean("piggy", false) {
        this.translation = "customscoreboard.config.lines.piggy"
    }

    val bankAlwaysCompact by boolean(false) {
        this.translation = "customscoreboard.config.lines.bank_always_compact"
    }

    val coopBankLayout by enum(ElementBank.CoopBankLayout.PERSONAL_COOP) {
        this.translation = "customscoreboard.config.lines.coop_bank_layout"
    }

    val petPrefix by boolean(true) {
        this.translation = "customscoreboard.config.lines.pet_prefix"
    }

    val showPetMax by boolean("pet_max", true) {
        this.translation = "customscoreboard.config.lines.pet_max"
    }

    val magicalPower by boolean(true) {
        this.translation = "customscoreboard.config.lines.magical_power"
        this.shPath = "display.maxwell.showMagicalPower"
    }

    val slayerLevel by boolean(true) {
        this.translation = "customscoreboard.config.lines.slayer_level"
    }

    val powderDisplay by enum(ElementPowder.PowderDisplay.CURRENT) {
        this.translation = "customscoreboard.config.lines.powder_display"
        this.shPath = "display.powderDisplay"
        this.shMapper = {
            when (it.asString) {
                "AVAILABLE" -> ElementPowder.PowderDisplay.CURRENT
                else -> valueOfOrNull<ElementPowder.PowderDisplay>(it.asString) ?: ElementPowder.PowderDisplay.CURRENT
            }
        }
    }

    val showHypixelPowder by boolean("hypixel_powder", true) {
        this.translation = "customscoreboard.config.lines.hypixel_powder"
    }

    val time24hFormat by boolean(false) {
        this.translation = "customscoreboard.config.lines.time_24h_format"
        this.shPath = "display.skyblockTime24hFormat"
    }

    val smoothTime by boolean(true) {
        this.translation = "customscoreboard.config.lines.smooth_time"
        this.shPath = "display.skyblockTimeExactMinutes"
    }
}
