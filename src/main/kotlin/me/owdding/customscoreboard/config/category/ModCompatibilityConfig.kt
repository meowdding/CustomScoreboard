package me.owdding.customscoreboard.config.category

import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.compat.ConfigTransfer
import me.owdding.customscoreboard.compat.ScoreboardOverhaulCompat
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor

object ModCompatibilityConfig : CategoryKt("compatibility") {

    override val name = Literal("Mod Compatibility")
    override val baseTranslation: String = "customscoreboard.config.compatibility"

    init {
        separator {
            this.title = "scoreboard_overhaul"
            this.description = "scoreboard_overhaul.desc"
        }
    }

    val scoreboardOverhaul by boolean(true) {
        this.translation = "scoreboard_overhaul.toggle"
    }

    val skyblockLevelColor by boolean(false) {
        this.translation = "scoreboard_overhaul.skyblock_level_color"
    }

    init {
        button {
            this.title = "scoreboard_overhaul.configbutton"
            this.description = "scoreboard_overhaul.configbutton.desc"
            this.text = "scoreboard_overhaul.configbutton.text"
            this.onClick {
                ScoreboardOverhaulCompat.openConfig()
            }
        }

        separator {
            this.title = "skyhanni"
            this.description = "skyhanni.desc"
        }

        button {
            this.title = "skyhanni.button"
            this.description = "skyhanni.button.desc"
            this.text = "skyhanni.button.text"
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
        this.translation = "skyhanni.override"
    }

}
