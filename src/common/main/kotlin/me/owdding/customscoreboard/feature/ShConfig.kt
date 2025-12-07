package me.owdding.customscoreboard.feature

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigObjectEntryElement
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import com.teamresourceful.resourcefulconfigkt.api.ObjectKt
import com.teamresourceful.resourcefulconfigkt.api.builders.CategoryBuilder
import com.teamresourceful.resourcefulconfigkt.api.builders.EntriesBuilder
import com.teamresourceful.resourcefulconfigkt.api.builders.NumberBuilder
import com.teamresourceful.resourcefulconfigkt.api.builders.StringBuilder
import com.teamresourceful.resourcefulconfigkt.api.builders.TypeBuilder
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.utils.RconfigKtUtils
import me.owdding.customscoreboard.utils.Utils.unsafeCast
import tech.thatgravyboat.skyblockapi.utils.json.getPath

interface ShConfig {
    val configMoves: MutableMap<String, String>
    val configMappers: MutableMap<String, (JsonElement) -> Any>
    val TypeBuilder.id: String
        get() = RconfigKtUtils.getId(this)

    var TypeBuilder.shPath: String?
        get() = configMoves[this.id]
        set(value) {
            configMoves[this.id] = value ?: return
        }

    var TypeBuilder.shMapper
        get() = configMappers[this.id]
        set(value) {
            configMappers[this.id] = value ?: return
        }

    var <T> NumberBuilder<T>.shMapper: ((JsonElement) -> Number)? where T : Number, T : Comparable<T>
        get() = configMappers[this.id].unsafeCast()
        set(value) {
            configMappers[this.id] = value ?: return
        }

    var StringBuilder.stringMapper: ((JsonElement) -> String)?
        get() = configMappers[this.id].unsafeCast()
        set(value) {
            configMappers[this.id] = value ?: return
        }

    fun self(): EntriesBuilder
    fun transfer(shConfig: JsonObject) {
        RconfigKtUtils.getElements(self()).filterIsInstance<ResourcefulConfigEntryElement>().forEach {
            val element = (it.entry() as? ResourcefulConfigValueEntry) ?: return@forEach
            val shPath = this.configMoves[it.id()] ?: return@forEach
            val mapper = this.configMappers[it.id()] ?: { json ->
                when (element.type()) {
                    EntryType.INTEGER -> json.asInt
                    EntryType.FLOAT -> json.asFloat
                    EntryType.LONG -> json.asLong
                    EntryType.STRING -> json.asString
                    EntryType.BOOLEAN -> json.asBoolean
                    EntryType.ENUM -> throw RuntimeException("Can't automatically resolve enum type ${it.id()}!")
                    else -> TODO("other stuff")
                }
            }
            val jsonElement = shConfig.getPath(shPath) ?: return@forEach
            val result = mapper(jsonElement)
            val transferResult = if (element.isArray) {
                when (result) {
                    is List<*> -> element.setArray(result.toTypedArray())
                    is Array<*> -> element.setArray(result)
                    else -> false
                }
            } else {
                try {
                    when (element.type()) {
                        EntryType.INTEGER -> element.setInt(result.unsafeCast())
                        EntryType.FLOAT -> element.setFloat(result.unsafeCast())
                        EntryType.LONG -> element.setLong(result.unsafeCast())
                        EntryType.STRING -> element.setString(result.unsafeCast())
                        EntryType.BOOLEAN -> element.setBoolean(result.unsafeCast())
                        EntryType.ENUM -> element.setEnum(result.unsafeCast())
                        else -> TODO("other stuff")
                    }
                } catch (e: Exception) {
                    throw RuntimeException("Failed to transfer property $shPath", e)
                }
            }

            if (!transferResult) {
                Main.warn("Failed to transfer sh config entry $shPath")
            }
        }

        RconfigKtUtils.getElements(self()).filterIsInstance<ResourcefulConfigObjectEntryElement>().forEach {
            (it.entry().instance() as? ShConfig)?.transfer(shConfig)
        }

        val self = self() as? CategoryBuilder ?: return
        RconfigKtUtils.getCategories(self).values.filterIsInstance<ShConfig>().forEach {
            it.transfer(shConfig)
        }
    }
}

abstract class ShTransferableObject() : ObjectKt(), ShConfig {
    override val configMoves = mutableMapOf<String, String>()
    override val configMappers = mutableMapOf<String, (JsonElement) -> Any>()
    override fun self(): ObjectKt = this
}

abstract class ShTransferableConfig(file: String) : ConfigKt(file), ShConfig {
    override val configMoves = mutableMapOf<String, String>()
    override val configMappers = mutableMapOf<String, (JsonElement) -> Any>()
    override fun self(): CategoryBuilder = this
}

abstract class ShTransferableCategory(id: String) : CategoryKt(id), ShConfig {
    override val configMoves = mutableMapOf<String, String>()
    override val configMappers = mutableMapOf<String, (JsonElement) -> Any>()
    override fun self(): CategoryBuilder = this
}
