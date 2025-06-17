package me.owdding.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPowder
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementQuiver
import me.owdding.customscoreboard.utils.DateFormat

object LinesConfig : CategoryKt("Line Modification") {

    val dateFormat by enum(DateFormat.US_SLASH_MMDDYYYY) {
        this.translation = "customscoreboard.config.lines.date_format"
    }

    val showBitsAvailable by boolean("bits_available", true) {
        this.translation = "customscoreboard.config.lines.bits_available"
    }

    val coloredMonth by boolean("colored_month", true) {
        this.translation = "customscoreboard.config.lines.colored_month"
    }

    val showProfileName by boolean("profile_name", false) {
        this.translation = "customscoreboard.config.lines.profile_name"
    }

    val showMayorTime by boolean("mayor_time", true) {
        this.translation = "customscoreboard.config.lines.mayor_time"
    }

    val showMayorPerks by boolean("mayor_perks", true) {
        this.translation = "customscoreboard.config.lines.mayor_perks"
    }

    val showMinister by boolean("mayor_minister", true) {
        this.translation = "customscoreboard.config.lines.mayor_minister"
    }

    val showAllActiveEvents by boolean("all_events", true) {
        this.translation = "customscoreboard.config.lines.all_events"
    }

    val useHypixelTitle by boolean("hypixel_title", true) {
        this.translation = "customscoreboard.config.lines.hypixel_title"
    }

    val showPartyEverywhere by boolean("party_everywhere", true) {
        this.translation = "customscoreboard.config.lines.party_everywhere"
    }

    val maxPartyMembers by int("max_party", 5) {
        this.translation = "customscoreboard.config.lines.max_party"
        this.slider = true
        this.range = 1..10
    }

    val showPartyLeader by boolean("party_leader", true) {
        this.translation = "customscoreboard.config.lines.party_leader"
    }

    val condenseConsecutiveSeparators by boolean("consecutive_separator", true) {
        this.translation = "customscoreboard.config.lines.consecutive_separator"
    }

    val hideSeparatorsAtStartEnd by boolean("separators_start_end", true) {
        this.translation = "customscoreboard.config.lines.separators_start_end"
    }

    val colorArrowAmount by boolean("color_arrow", true) {
        this.translation = "customscoreboard.config.lines.color_arrow"
    }

    val arrowDisplay by enum("arrow_display", ElementQuiver.ArrowDisplay.NUMBER) {
        this.translation = "customscoreboard.config.lines.arrow_display"
    }

    val showPiggy by boolean("piggy", false) {
        this.translation = "customscoreboard.config.lines.piggy"
    }

    val showPetMax by boolean("pet_max", true) {
        this.translation = "customscoreboard.config.lines.pet_max"
    }

    val powderDisplay by enum(ElementPowder.PowderDisplay.CURRENT) {
        this.translation = "customscoreboard.config.lines.powder_display"
    }

    val showHypixelPowder by boolean("hypixel_powder", true) {
        this.translation = "customscoreboard.config.lines.hypixel_powder"
    }
}
