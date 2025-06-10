package me.owdding.customscoreboard.feature

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.profile.ProfileChangeEvent
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.command
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.hover

@Module
object SkyHanniCompat {

    var isSkyhanniCustomScoreboardEnabled = false

    @Subscription
    fun onProfile(event: ProfileChangeEvent) {
        if (!isSkyhanniCustomScoreboardEnabled || !MainConfig.enabled) return

        Text.of("Both Skyhanni's Custom Scoreboard and CustomScoreboard are enabled.") {
            color = TextColor.RED
        }.sendWithPrefix()
        Text.of("Click here to open SkyHanni's Custom Scoreboard settings.") {
            color = TextColor.BLUE
            command = "/skyhanni custom scoreboard"
            hover = Text.of("Open SkyHanni Config")
        }.send()
        Text.of("Click here to open Custom Scoreboard's settings.") {
            color = TextColor.BLUE
            command = "/customscoreboard"
            hover = Text.of("Open Custom Scoreboard Config")
        }.send()
    }

}
