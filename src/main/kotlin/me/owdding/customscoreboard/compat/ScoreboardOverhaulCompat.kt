//? scoreboard_overhaul {
package me.owdding.customscoreboard.compat

import me.jfenn.scoreboardoverhaul.common.config.ConfigManager
import me.jfenn.scoreboardoverhaul.common.config.ConfigScreenBuilder
import me.jfenn.scoreboardoverhaul.common.data.ScoreInfo
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McScreen
import tech.thatgravyboat.skyblockapi.utils.text.Text


object ScoreboardOverhaulCompat {
    private val log = LoggerFactory.getLogger(ScoreboardOverhaulCompat::class.java)

    val isInstalled = McClient.anyModInstalled("scoreboard-overhaul")
    val yaclInstalled = McClient.anyModInstalled("yet_another_config_lib_v3")

    @JvmStatic
    fun createInfo(string: String, component: Component, int: Int): ScoreInfo = ScoreInfo(string, component, int)

    fun openConfig() {
        if (!isInstalled) {
            Text.of("ScoreboardOverhaul is not installed!").sendWithPrefix()
            return
        }
        if (!yaclInstalled) {
            Text.of("Yacl is not installed!").sendWithPrefix()
            return
        }

        McClient.setScreenAsync {
            ConfigScreenBuilder(ConfigManager(log), ConfigManager.instance ?: return@setScreenAsync null).create(McScreen.self ?: return@setScreenAsync null)
        }
    }
}
//? }
