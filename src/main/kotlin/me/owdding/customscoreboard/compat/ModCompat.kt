package me.owdding.customscoreboard.compat

import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.config.Config
import me.owdding.customscoreboard.config.category.ModCompatibilityConfig
import me.owdding.customscoreboard.utils.RegisterCustomScoreboardCommandEvent
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import me.owdding.ktmodules.Module
import me.owdding.lib.builder.ComponentFactory
import net.fabricmc.loader.api.FabricLoader
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.profile.ProfileChangeEvent
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@Module
object ModCompat {

    var isSkyhanniCustomScoreboardEnabled = false
    var isOverhaulEnabled = false

    val isScoreboardOverhaulLoaded = FabricLoader.getInstance().isModLoaded("scoreboard-overhaul")

    fun isScoreboardOverhaulEnabled() = isScoreboardOverhaulLoaded && isOverhaulEnabled

    @Subscription
    fun onProfile(event: ProfileChangeEvent) {
        if (!isSkyhanniCustomScoreboardEnabled || !Config.enabled) return

        Text.of("Both Skyhanni's Custom Scoreboard and CustomScoreboard are enabled.") {
            color = TextColor.RED
            append("Automatically stopped SkyHanni's Scoreboard from Rendering. ") {
                color = TextColor.YELLOW
            }
            append("Disable SkyHanni's Custom Scoreboard to disable this message.") {
                color = TextColor.RED
            }
        }.sendWithPrefix()
    }

    @Subscription
    fun onCommand(event: RegisterCustomScoreboardCommandEvent) {
        event.registerWithCallback("debug") {
            ComponentFactory.multiline {
                string("Mod Compatibility Debug Info ${CustomScoreboardMod.VERSION}") {
                    color = TextColor.GOLD
                }

                string("------------") { color = TextColor.DARK_GRAY }
                string("- Skyhanni's Custom Scoreboard Enabled: ") {
                    color = TextColor.YELLOW
                    append(isSkyhanniCustomScoreboardEnabled) {
                        color = if (isSkyhanniCustomScoreboardEnabled) TextColor.GREEN else TextColor.RED
                    }
                }
                string("- Override Skyhanni's Scoreboard: ") {
                    color = TextColor.YELLOW
                    append(ModCompatibilityConfig.overrideSkyHanniScoreboard) {
                        color = if (ModCompatibilityConfig.overrideSkyHanniScoreboard) TextColor.GREEN else TextColor.RED
                    }
                }

                string("------------") { color = TextColor.DARK_GRAY }
                string("- Scoreboard Overhaul Loaded: ") {
                    color = TextColor.YELLOW
                    append(isScoreboardOverhaulLoaded) {
                        color = if (isScoreboardOverhaulLoaded) TextColor.GREEN else TextColor.RED
                    }
                }
                string("- Scoreboard Overhaul Enabled: ") {
                    color = TextColor.YELLOW
                    append(isOverhaulEnabled) {
                        color = if (isOverhaulEnabled) TextColor.GREEN else TextColor.RED
                    }
                }
                string("- Override Scoreboard Overhaul: ") {
                    color = TextColor.YELLOW
                    append(ModCompatibilityConfig.scoreboardOverhaul) {
                        color = if (ModCompatibilityConfig.scoreboardOverhaul) TextColor.GREEN else TextColor.RED
                    }
                }

                string("------------") { color = TextColor.DARK_GRAY }
                string("- Current Config Enabled: ") {
                    color = TextColor.YELLOW
                    append(Config.enabled) {
                        color = if (Config.enabled) TextColor.GREEN else TextColor.RED
                    }
                }
                string("- Hide Hypixel Scoreboard: ") {
                    color = TextColor.YELLOW
                    append(Config.hideHypixelScoreboard) {
                        color = if (Config.hideHypixelScoreboard) TextColor.GREEN else TextColor.RED
                    }
                }
                string("- Outside Skyblock: ") {
                    color = TextColor.YELLOW
                    append(Config.outsideSkyBlock) {
                        color = if (Config.outsideSkyBlock) TextColor.GREEN else TextColor.RED
                    }
                }

                string("------------") { color = TextColor.DARK_GRAY }
            }.sendWithPrefix()
        }
    }

}
