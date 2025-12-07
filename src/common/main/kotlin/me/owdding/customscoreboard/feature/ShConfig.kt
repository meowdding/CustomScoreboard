package me.owdding.customscoreboard.feature

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import com.teamresourceful.resourcefulconfigkt.api.builders.CategoryBuilder
import com.teamresourceful.resourcefulconfigkt.api.builders.NumberBuilder
import com.teamresourceful.resourcefulconfigkt.api.builders.StringBuilder
import com.teamresourceful.resourcefulconfigkt.api.builders.TypeBuilder
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

    fun self(): CategoryBuilder
    fun transfer(shConfig: JsonObject) {
        RconfigKtUtils.getElements(self()).filterIsInstance<ResourcefulConfigEntryElement>().forEach {
            val element = (it.entry() as? ResourcefulConfigValueEntry) ?: return@forEach
            val shPath = this.configMoves[it.id()] ?: return@forEach
            val mapper = this.configMappers[it.id()] ?: { json ->
                when (element.type()) {
                    EntryType.FLOAT -> json.asFloat
                    EntryType.INTEGER -> json.asInt
                    else -> TODO("other stuff")
                }
            }
            val jsonElement = shConfig.getPath(shPath) ?: return@forEach
            val result = mapper(jsonElement)
            when (element.type()) {
                EntryType.FLOAT -> element.float = result.unsafeCast()
                EntryType.INTEGER -> element.int = result.unsafeCast()
                else -> TODO("other stuff")
            }
        }

        RconfigKtUtils.getCategories(self()).values.filterIsInstance<ShConfig>().forEach {
            it.transfer(shConfig)
        }
    }
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
