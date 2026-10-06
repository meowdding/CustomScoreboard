package me.owdding.customscoreboard.compat

import me.jfenn.scoreboardoverhaul.api.ScoreboardApi
import me.jfenn.scoreboardoverhaul.api.data.ObjectiveInfo
import me.jfenn.scoreboardoverhaul.api.data.ScoreInfo
import me.owdding.customscoreboard.config.category.ModCompatibilityConfig
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McScreen
import tech.thatgravyboat.skyblockapi.utils.text.CommonText
import tech.thatgravyboat.skyblockapi.utils.text.Text

object ScoreboardOverhaulCompat {
    val isInstalled = McClient.anyModInstalled("scoreboard-overhaul")
    val yaclInstalled = McClient.anyModInstalled("yet_another_config_lib_v3")

    private var isEnabled = false
    fun isEnabled() = isInstalled && isEnabled

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
            ScoreboardApi.INSTANCE?.buildConfigScreen(McScreen.self ?: return@setScreenAsync null) ?: return@setScreenAsync null
        }
    }

    fun updateApi() {
        if (!isInstalled) return
        val api = ScoreboardApi.INSTANCE ?: return

        isEnabled = api.config.isEnabled

        if (CustomScoreboardRenderer.renderScoreboardOverhaul() && CustomScoreboardRenderer.shouldUseCustomLines() && CustomScoreboardRenderer.lines.size > 1) {
            val lines = CustomScoreboardRenderer.lines
            val objective = ObjectiveInfo(
                id = "customscoreboard",
                displayName = lines.first().component,
            )
            val scores = lines.drop(1).mapIndexed { index, line ->
                val scoreValue = lines.size - 2 - index
                ScoreInfo(
                    id = "Line$scoreValue",
                    displayName = line.component,
                    value = scoreValue,
                    score = CommonText.EMPTY,
                    expandedScore = CommonText.EMPTY,
                )
            }
            api.setScoreboard(objective, scores)
        } else {
            resetApi()
        }

        val color = if (ModCompatibilityConfig.skyblockLevelColor && LocationAPI.isOnSkyBlock) {
            ProfileAPI.getLevelColor()
        } else null
        api.setAutoTeamColor(color)
    }

    fun resetApi() {
        val api = ScoreboardApi.INSTANCE ?: return
        api.setScoreboard(null, null)
        api.setAutoTeamColor(null)
    }
}
