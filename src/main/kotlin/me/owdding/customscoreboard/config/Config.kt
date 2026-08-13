package me.owdding.customscoreboard.config

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.compat.SkyHanniOption.shPath
import me.owdding.customscoreboard.config.category.BackgroundConfig
import me.owdding.customscoreboard.config.category.CustomizationConfig
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.config.category.ModCompatibilityConfig
import me.owdding.customscoreboard.elements.CopperElement
import me.owdding.customscoreboard.elements.KernelsElement
import me.owdding.customscoreboard.elements.MayorElement
import me.owdding.customscoreboard.elements.SowdustElement
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.Utils.convertLegacyToPlaceholder
import me.owdding.customscoreboard.utils.Utils.updateDisplay
import net.minecraft.util.ARGB
import java.util.function.UnaryOperator
import kotlin.math.pow

object Config : ConfigKt("customscoreboard/config") {

    override val name = TranslatableValue("Custom Scoreboard Config")
    override val description = TranslatableValue("by j10a1n15. Version ${CustomScoreboardMod.VERSION}")
    override val links: Array<ResourcefulConfigLink> = arrayOf(
        ResourcefulConfigLink.create(
            "https://meowdd.ing/discord",
            "discord",
            TranslatableValue("Discord"),
        ),
        ResourcefulConfigLink.create(
            "https://modrinth.com/mod/skyblock-custom-scoreboard",
            "modrinth",
            TranslatableValue("Modrinth"),
        ),
        ResourcefulConfigLink.create(
            "https://github.com/meowdding/CustomScoreboard",
            "code",
            TranslatableValue("GitHub"),
        ),
    )

    //region Patches
    override val patches: Map<Int, UnaryOperator<JsonObject>> = mapOf(
        1 to UnaryOperator { json ->
            json.getAsJsonArray("events").add(ScoreboardEventEntry.FORAGING.name)
            json
        },
        3 to UnaryOperator { json ->
            json.getAsJsonArray("events").add(ScoreboardEventEntry.ANNIVERSARY.name)
            json
        },
        4 to UnaryOperator { json ->
            val lines = json.getAsJsonObject("Line Modification")

            val perksEnum = if (lines.get("mayor_perks").asBoolean) MayorElement.PerkDisplay.ALL
            else MayorElement.PerkDisplay.OFF
            lines.addProperty("mayorPerksDisplay", perksEnum.name)

            val ministerEnum = if (lines.get("mayor_minister").asBoolean) MayorElement.MinisterDisplay.FULL
            else MayorElement.MinisterDisplay.OFF
            lines.addProperty("ministerDisplay", ministerEnum.name)

            lines.add("line_modification", lines)

            json
        },
        4 to UnaryOperator { json ->
            val items = json.getAsJsonArray("appearance").toMutableList()
            val copperIndex = items.indexOfFirst { it.asString == CopperElement.id }
            val newElement = JsonPrimitive(SowdustElement.id)

            if (copperIndex != -1) {
                items.add(copperIndex + 1, newElement)
            } else {
                items.add(newElement)
            }

            val newAppearance = JsonArray()
            items.forEach { newAppearance.add(it) }
            json.add("appearance", newAppearance)

            json
        },
        5 to UnaryOperator { json ->
            val overhaul = json["scoreboardOverhaul"].asBoolean
            json.remove("scoreboardOverhaul")
            json.add(
                "compatibility",
                JsonObject().apply {
                    addProperty("scoreboardOverhaul", overhaul)
                },
            )
            json
        },
        6 to UnaryOperator { json ->
            // Customization Page
            val customPage = JsonObject()

            val moveToCustom =
                listOf("appearance", "events", "tablistLines", "scale", "vertical_alignment", "horizontal_alignment", "chunkedStats", "statsPerLine")
            moveToCustom.forEach { if (json.has(it)) customPage.add(it, json.get(it)) }
            val oldLineModification = json.getAsJsonObject("line_modification")
            if (oldLineModification != null && oldLineModification.has("hypixel_title")) {
                customPage.add("useHypixelTitle", oldLineModification.get("hypixel_title"))
            }

            json.getAsJsonObject("title_options")?.let { title ->
                customPage.add("titleAlignment", title.get("alignment"))
                customPage.add("titleUseCustomText", title.get("use_custom_text"))
                customPage.add("titleText", title.get("custom_text"))
            }
            json.getAsJsonObject("footer_options")?.let { footer ->
                customPage.add("footerAlignment", footer.get("alignment"))
                customPage.add("footerUseCustomText", footer.get("use_custom_text"))
                customPage.add("footerText", footer.get("custom_text"))
            }

            json.add("customization", customPage)


            // Lines Page
            val lineModification = json.getAsJsonObject("line_modification") ?: JsonObject().also { json.add("line_modification", it) }
            val moveToLines = listOf("numberFormat", "numberDisplayFormat", "showActiveOnly", "showCurrencyGain")
            moveToLines.forEach { key ->
                if (json.has(key)) lineModification.add(key, json.remove(key))
            }

            json
        },
        7 to UnaryOperator { json ->
            val items = json.getAsJsonObject("customization").getAsJsonArray("appearance").toMutableList()
            val sowdustIndex = items.indexOfFirst { it.asString == SowdustElement.id }
            val newElement = JsonPrimitive(KernelsElement.id)

            if (sowdustIndex != -1) {
                items.add(sowdustIndex + 1, newElement)
            } else {
                items.add(newElement)
            }

            val newAppearance = JsonArray()
            items.forEach { newAppearance.add(it) }
            json.add("appearance", newAppearance)

            json
        },
        8 to UnaryOperator { json -> // GALATEA -> FORAGING
            val items = json.getAsJsonObject("customization").getAsJsonArray("events").toMutableList()
            val newArray = JsonArray()
            items.forEach { item ->
                if (item.asString == "GALATEA") newArray.add(ScoreboardEventEntry.FORAGING.name) else newArray.add(item)
            }
            json.getAsJsonObject("customization").add("events", newArray)

            json
        },
        9 to UnaryOperator { json ->
            json.getAsJsonObject("customization")?.let { customPage ->
                customPage.get("titleText")?.asString?.let { oldTitle ->
                    customPage.add("titleText", JsonArray().apply { oldTitle.lines().map(::convertLegacyToPlaceholder).forEach(::add) })
                }
                customPage.get("footerText")?.asString?.let { oldFooter ->
                    customPage.add("footerText", JsonArray().apply { oldFooter.lines().map(::convertLegacyToPlaceholder).forEach(::add) })
                }
                customPage.get("alphaFooterText")?.asString?.let { oldFooter ->
                    customPage.add("alphaFooterText", JsonArray().apply { oldFooter.lines().map(::convertLegacyToPlaceholder).forEach(::add) })
                }
            }
            json
        },
        10 to UnaryOperator { json ->
            json.getAsJsonObject("Background").apply {
                val color = get("backgroundColor").asInt
                addProperty("backgroundColor", ARGB.color(ARGB.alpha(color).toFloat().pow(2), color))
            }
            json
        },
    )

    override val version = patches.maxOf { it.key } + 1
    //endregion

    private val translationPath = "customscoreboard.config.main"

    init {
        category(CustomizationConfig)
        category(BackgroundConfig)
        category(LinesConfig)
        category(ModCompatibilityConfig)
    }

    var enabled by boolean(true) {
        this.translation = "$translationPath.enabled"
    }

    val hideWhenTab by boolean(false) {
        this.translation = "$translationPath.hide_when_tab"
    }

    val hideWhenChat by boolean(false) {
        this.translation = "$translationPath.hide_when_chat"
    }

    val hideHypixelScoreboard by boolean("hide_hypixel", true) {
        this.translation = "$translationPath.hide_hypixel"
        this.shPath = "display.hideVanillaScoreboard"
    }

    val textShadow by boolean("text_shadow", true) {
        this.translation = "$translationPath.text_shadow"
    }

    val customLines by boolean(true) {
        this.translation = "$translationPath.custom_lines"
        this.shPath = "display.useCustomLines"
    }.updateDisplay()

    val actions by boolean(true) {
        this.translation = "$translationPath.actions"
    }.updateDisplay()

    val outsideSkyBlock by boolean(false) {
        this.translation = "$translationPath.outside_skyblock"
    }

    val updateEveryTick by boolean(false) {
        this.translation = "$translationPath.update_every_tick"
    }

    val updateNotification by boolean("update_notification", true) {
        this.translation = "$translationPath.update_notification"
    }

}
