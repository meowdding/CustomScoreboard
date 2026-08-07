package me.owdding.customscoreboard.compat

import com.google.gson.JsonObject
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.compat.SkyHanniOption.transfer
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.json.Json.readJson
import tech.thatgravyboat.skyblockapi.utils.json.getPath
import kotlin.io.path.exists
import kotlin.io.path.readText

object ConfigTransfer {

    fun transfer() {
        val shConfig = shConfig() ?: return
        (shConfig.getPath("gui.customScoreboard") as? JsonObject)?.let { CustomScoreboardMod.config.transfer(it) }
    }

    fun shConfig(): JsonObject? {
        val file = McClient.config.resolve("skyhanni/config.json")
        if (!file.exists()) return null
        val content = file.readText()
        return content.readJson()
    }

}
