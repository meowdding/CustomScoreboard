package me.owdding.customscoreboard.utils

import me.jfenn.scoreboardoverhaul.common.data.ScoreInfo
import net.minecraft.network.chat.Component

object ScoreboardOverhaulUtils {

    @JvmStatic
    fun createInfo(string: String, component: Component, int: Int): ScoreInfo = ScoreInfo(string, component, int)

}
