package me.owdding.customscoreboard.feature

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType
import com.teamresourceful.resourcefulconfig.api.types.options.Option
import com.teamresourceful.resourcefulconfigkt.api.builders.TypeBuilder
import me.owdding.customscoreboard.Main
import tech.thatgravyboat.skyblockapi.utils.json.getPath

object SkyHanniOption {

    private val PATH_OPTION = Option.create<String>()
    private val MAPPER_OPTION = Option.create<(JsonElement) -> Any>()

    var TypeBuilder.shPath: String
        get() = error("Unsupported")
        set(value) {
            this.options += PATH_OPTION to value
        }

    var TypeBuilder.shMapper: ((JsonElement) -> Any)
        get() = error("Unsupported")
        set(value) {
            this.options += MAPPER_OPTION to value
        }

    fun ResourcefulConfig.transfer(shConfig: JsonObject) {
        this.elements().transfer(shConfig)
        for ((_, category) in this.categories()) {
            category.transfer(shConfig)
        }
    }

    private fun List<ResourcefulConfigElement>.transfer(shConfig: JsonObject) {
        val entries = this.mapNotNull { element -> (element as? ResourcefulConfigEntryElement)?.entry()?.let { element.id() to it } }

        for ((id, entry) in entries) {
            when (entry) {
                is ResourcefulConfigObjectEntry -> entry.elements().transfer(shConfig)
                is ResourcefulConfigValueEntry -> {
                    val shPath = entry.options().getOption(PATH_OPTION) ?: continue
                    val mapper = entry.options().getOption(MAPPER_OPTION) ?: { json ->
                        when (entry.type()) {
                            EntryType.INTEGER -> json.asInt
                            EntryType.FLOAT -> json.asFloat
                            EntryType.LONG -> json.asLong
                            EntryType.STRING -> json.asString
                            EntryType.BOOLEAN -> json.asBoolean
                            else -> error("Can't automatically map type ${entry.type()} for $id")
                        }
                    }

                    val result = mapper.invoke(shConfig.getPath(shPath) ?: continue)
                    val transferResult: Boolean = if (entry.isArray) {
                        when (result) {
                            is List<*> -> entry.setArray(result.toTypedArray())
                            is Array<*> -> entry.setArray(result)
                            else -> false
                        }
                    } else {
                        when (entry.type()) {
                            EntryType.INTEGER -> (result as? Int)?.let(entry::setInt) ?: false
                            EntryType.FLOAT -> (result as? Float)?.let(entry::setFloat) ?: false
                            EntryType.LONG -> (result as? Long)?.let(entry::setLong) ?: false
                            EntryType.STRING -> (result as? String)?.let(entry::setString) ?: false
                            EntryType.BOOLEAN -> (result as? Boolean)?.let(entry::setBoolean) ?: false
                            EntryType.ENUM -> (result as? Enum<*>)?.let(entry::setEnum) ?: false
                            else -> error("Type unsupported for transfers: ${entry.type()}")
                        }
                    }

                    if (!transferResult) {
                        Main.warn("Failed to transfer sh config entry $shPath")
                    }
                }
            }
        }
    }

}
