package me.owdding.customscoreboard.feature

import com.google.gson.JsonObject
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.json.Json.readJson
import tech.thatgravyboat.skyblockapi.utils.json.getPath
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.reflect.KMutableProperty

object ConfigTransfer {

    private val configMoves = mutableMapOf<KMutableProperty<*>, String>()
    private const val BASE = "gui.customScoreboard."

    fun transfer() {
        val shConfig = shConfig() ?: return
        for ((type, path) in configMoves) {
            val element = shConfig.getPath(BASE + path)
            println("Transferring config for $path to $element")
            type.setter.call(type.getter.call(), element)
        }
    }

    fun shConfig(): JsonObject? {
        val file = McClient.config.resolve("skyhanni/config.json")
        if (!file.exists()) return null
        val content = file.readText()
        return content.readJson()
    }

}
