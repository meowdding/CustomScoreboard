package me.owdding.customscoreboard.feature

import com.google.gson.JsonObject
import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.json.Json.readJson
import kotlin.io.path.exists
import kotlin.io.path.readText

object ConfigTransfer {

    fun transfer() {
        val shConfig = shConfig() ?: return
        MainConfig.transfer(shConfig)
        BackgroundConfig.transfer(shConfig)
        LinesConfig.transfer(shConfig)
    }

    fun shConfig(): JsonObject? {
        val file = McClient.config.resolve("skyhanni/config.json")
        if (!file.exists()) return null
        val content = file.readText()
        return content.readJson()
    }

}
