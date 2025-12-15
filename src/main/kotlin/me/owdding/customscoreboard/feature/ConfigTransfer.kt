package me.owdding.customscoreboard.feature

import com.google.gson.JsonObject
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.feature.SkyHanniOption.transfer
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.json.Json.readJson
import tech.thatgravyboat.skyblockapi.utils.json.getPath
import kotlin.io.path.exists
import kotlin.io.path.readText

object ConfigTransfer {

    fun transfer() {
        val shConfig = shConfig() ?: return
        (shConfig.getPath("gui.customScoreboard") as? JsonObject)?.let { Main.config.transfer(it) }
    }

    fun shConfig(): JsonObject? {
        val file = McClient.config.resolve("skyhanni/config.json")
        if (!file.exists()) return null
        val content = file.readText()
        return content.readJson()
    }

}
