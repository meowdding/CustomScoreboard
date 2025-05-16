package gay.j10a1n15.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import gay.j10a1n15.customscoreboard.feature.customscoreboard.elements.ElementQuiver

object LinesConfig : CategoryKt("Line Modification") {

    val showBitsAvailable by boolean("bits_available", true) {
        this.translation = "config.cs.lines.bits_available"
    }

    val coloredMonth by boolean("colored_month", true) {
        this.translation = "config.cs.lines.colored_month"
    }

    val showProfileName by boolean("profile_name", false) {
        this.translation = "config.cs.lines.profile_name"
    }

    val showMayorTime by boolean("mayor_time", true) {
        this.translation = "config.cs.lines.mayor_time"
    }

    val showMayorPerks by boolean("mayor_perks", true) {
        this.translation = "config.cs.lines.mayor_perks"
    }

    val showMinister by boolean("mayor_minister", true) {
        this.translation = "config.cs.lines.mayor_minister"
    }

    val showAllActiveEvents by boolean("all_events", true) {
        this.translation = "config.cs.lines.all_events"
    }

    val useHypixelTitle by boolean("hypixel_title", true) {
        this.translation = "config.cs.lines.hypixel_title"
    }

    val showPartyEverywhere by boolean("party_everywhere", true) {
        this.translation = "config.cs.lines.party_everywhere"
    }

    val maxPartyMembers by int("max_party", 5) {
        this.translation = "config.cs.lines.max_party"
        this.slider = true
        this.range = 1..10
    }

    val showPartyLeader by boolean("party_leader", true) {
        this.translation = "config.cs.lines.party_leader"
    }

    val condenseConsecutiveSeparators by boolean("consecutive_separator", true) {
        this.translation = "config.cs.lines.consecutive_separator"
    }

    val hideSeparatorsAtStartEnd by boolean("separators_start_end", true) {
        this.translation = "config.cs.lines.separators_start_end"
    }

    val colorArrowAmount by boolean("color_arrow", true) {
        this.translation = "config.cs.lines.color_arrow"
    }

    val arrowDisplay by enum("arrow_display", ElementQuiver.ArrowDisplay.NUMBER) {
        this.translation = "config.cs.lines.arrow_display"
    }

    val showPiggy by boolean("piggy", false) {
        this.translation = "config.cs.lines.piggy"
    }

    val showPetMax by boolean("pet_max", true) {
        this.translation = "config.cs.lines.pet_max"
    }

    val showHypixelPowder by boolean("hypixel_powder", true) {
        this.translation = "config.cs.lines.hypixel_powder"
    }
}
