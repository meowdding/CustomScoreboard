package me.owdding.customscoreboard.config

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
            Config.enabled = !Config.enabled
        } else {
            toggleKeyHold = false
        }
    }
}
