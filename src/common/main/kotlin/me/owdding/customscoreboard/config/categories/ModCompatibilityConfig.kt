package me.owdding.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.feature.ConfigTransfer

object ModCompatibilityConfig : CategoryKt("compatibility") {

    override val name = Literal("Mod Compatibility")

    init {
        separator {
            this.title = "customscoreboard.config.compatibility.scoreboard_overhaul"
            this.description = "customscoreboard.config.compatibility.scoreboard_overhaul.desc"
        }
    }

    val scoreboardOverhaul by boolean(true) {
        this.translation = "customscoreboard.config.compatibility.scoreboard_overhaul.toggle"
    }

    init {
        separator {
            this.title = "customscoreboard.config.compatibility.skyhanni"
            this.description = "customscoreboard.config.compatibility.skyhanni.desc"
        }

        button {
            this.title = "customscoreboard.config.compatibility.skyhanni.button"
            this.description = "customscoreboard.config.compatibility.skyhanni.button.desc"
            this.text = "customscoreboard.config.compatibility.skyhanni.button.text"
            this.onClick {
                ConfigTransfer.transfer()
            }
        }
    }

    val overrideSkyHanniScoreboard by boolean(true) {
        this.translation = "customscoreboard.config.compatibility.skyhanni.override"
    }

}
