package me.owdding.customscoreboard.feature

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import me.owdding.ktmodules.Module
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
        if (!isSkyhanniCustomScoreboardEnabled || !MainConfig.enabled) return

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

}
