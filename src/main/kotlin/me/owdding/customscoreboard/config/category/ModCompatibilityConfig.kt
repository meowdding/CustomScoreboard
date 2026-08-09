package me.owdding.customscoreboard.config.category

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.compat.ConfigTransfer
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

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
                    CustomScoreboardMod.error("Failed to transfer config", it)
                    Text.of("Failed to transfer SkyHanni config: ${it.message}. Report this in the Discords", TextColor.RED).sendWithPrefix()
                }
            }
        }
    }

    val overrideSkyHanniScoreboard by boolean(true) {
        this.translation = "customscoreboard.config.compatibility.skyhanni.override"
    }

}
