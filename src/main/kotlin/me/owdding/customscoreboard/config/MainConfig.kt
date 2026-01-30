package me.owdding.customscoreboard.config

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import me.owdding.customscoreboard.config.categories.CustomizationConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.config.categories.ModCompatibilityConfig
import me.owdding.customscoreboard.feature.SkyHanniOption.shPath
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCopper
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMayor
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSowdust
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import java.util.function.UnaryOperator

object MainConfig : ConfigKt("customscoreboard/config") {

    override val name = TranslatableValue("Custom Scoreboard Config")
    override val description = TranslatableValue("by j10a1n15. Version ${Main.VERSION}")
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
        0 to UnaryOperator { json ->
            json.getAsJsonArray("events").add(ScoreboardEventEntry.GALATEA.name)
            json
        },
        1 to UnaryOperator { json ->
            json.getAsJsonArray("events").add(ScoreboardEventEntry.ANNIVERSARY.name)
            json
        },
        2 to UnaryOperator { json ->
            val lines = json.getAsJsonObject("Line Modification")

            val perksEnum = if (lines.get("mayor_perks").asBoolean) ElementMayor.PerkDisplay.ALL
            else ElementMayor.PerkDisplay.OFF
            lines.addProperty("mayorPerksDisplay", perksEnum.name)

            val ministerEnum = if (lines.get("mayor_minister").asBoolean) ElementMayor.MinisterDisplay.FULL
            else ElementMayor.MinisterDisplay.OFF
            lines.addProperty("ministerDisplay", ministerEnum.name)

            lines.add("line_modification", lines)

            json
        },
        3 to UnaryOperator { json ->
            val items = json.getAsJsonArray("appearance").toMutableList()
            val copperIndex = items.indexOfFirst { it.asString == ElementCopper.id }
            val newElement = JsonPrimitive(ElementSowdust.id)

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
        4 to UnaryOperator { json ->
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
        5 to UnaryOperator { json ->
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
    )

    override val version = patches.size
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
    }

    val outsideSkyBlock by boolean(false) {
        this.translation = "$translationPath.outside_skyblock"
    }

    val updateNotification by boolean("update_notification", true) {
        this.translation = "$translationPath.update_notification"
    }

}
