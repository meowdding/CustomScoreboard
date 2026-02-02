package me.owdding.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.Main
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

    val skyblockLevelColor by boolean(false) {
        this.translation = "customscoreboard.config.compatibility.scoreboard_overhaul.skyblock_level_color"
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
                runCatching {
                    ConfigTransfer.transfer()
                }.exceptionOrNull()?.let {
                    Main.error("Failed to transfer config", it)
                }
            }
        }
    }

    val overrideSkyHanniScoreboard by boolean(true) {
        this.translation = "customscoreboard.config.compatibility.skyhanni.override"
    }

}
