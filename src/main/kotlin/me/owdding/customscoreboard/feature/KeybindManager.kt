package me.owdding.customscoreboard.feature

import me.owdding.customscoreboard.config.CustomScoreboardKeybinds
import me.owdding.customscoreboard.config.MainConfig
import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.time.TickEvent

@Module
object KeybindManager {
    var toggleKeyHold = false

    @Subscription
    fun onKeyClick(event: TickEvent) {
        if (CustomScoreboardKeybinds.TOGGLE_KEY.isDown) {
            if (toggleKeyHold) return
            toggleKeyHold = true
            MainConfig.enabled = !MainConfig.enabled
        } else {
            toggleKeyHold = false
        }
    }
}
