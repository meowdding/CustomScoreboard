package me.owdding.customscoreboard.config

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toBaseElements
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toConfigStrings
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.config.objects.TitleOrFooterObject
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.TabWidgetHelper
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementArea
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBank
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBits
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCold
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCopper
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementDate
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementEvents
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementFooter
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementGems
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementHeat
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementIsland
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementLobby
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMayor
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMotes
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementNorthStars
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementObjective
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementParty
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPet
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPowder
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementProfile
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPurse
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementQuiver
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSeparator
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSlayer
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSoulflow
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSowdust
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTime
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTitle
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.NumberFormatType
import me.owdding.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import me.owdding.customscoreboard.utils.rendering.alignment.VerticalAlignment
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import java.util.function.UnaryOperator

object MainConfig : ConfigKt("customscoreboard/config") {

    override val name = TranslatableValue("Custom Scoreboard Config")
    override val description = TranslatableValue("by j10a1n15. Version ${Main.VERSION}")
    override val links: Array<ResourcefulConfigLink> = arrayOf(
        ResourcefulConfigLink.create(
            "https://discord.gg/FsRc2GUwZR",
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

    override val version = 4

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
    )
    //endregion

    init {
        category(BackgroundConfig)
        category(LinesConfig)
    }

    var enabled by boolean(true) {
        this.translation = "customscoreboard.config.enabled"
    }

    private val default = listOf(
        ElementTitle,
        ElementLobby,
        ElementSeparator,
        ElementDate,
        ElementTime,
        ElementIsland,
        ElementArea,
        ElementProfile,
        ElementSeparator,
        ElementPurse,
        ElementMotes,
        ElementBank,
        ElementBits,
        ElementCopper,
        ElementSowdust,
        ElementGems,
        ElementHeat,
        ElementCold,
        ElementNorthStars,
        ElementSoulflow,
        ElementSeparator,
        ElementObjective,
        ElementSlayer,
        ElementQuiver,
        ElementEvents,
        ElementPowder,
        ElementMayor,
        ElementParty,
        ElementPet,
        ElementFooter,
    ).map { it.id }

    val appearance by observable(
        transform(
            strings(*default.toTypedArray()) {
                this.translation = "customscoreboard.config.appearance"
                renderer = CUSTOM_DRAGGABLE_RENDERER
            },
            { it.toConfigStrings() },
            { it.asList().toBaseElements() },
        ),
    ) { _, _ ->
        CustomScoreboardRenderer.updateIslandCache()
    }

    val events by observable(
        draggable(*ScoreboardEventEntry.entries.toTypedArray()) {
            this.translation = "customscoreboard.config.events"
        },
    ) { _, _ ->
        CustomScoreboardRenderer.updateIslandCache()
    }

    val tablistLines by observable(
        draggable<TabWidget> {
            this.translation = "customscoreboard.config.tablist_lines"
        },
    ) { _, _ ->
        TabWidgetHelper.updateTablistLineCache()
    }

    val scale by double(1.0) {
        this.translation = "customscoreboard.config.scale"
        this.range = 0.1..2.0
        this.slider = true
    }

    val title = obj("title_options", TitleOrFooterObject()) {
        this.translation = "customscoreboard.config.title_options"
    }

    val footer = obj("footer_options", TitleOrFooterObject()) {
        this.translation = "customscoreboard.config.footer_options"
    }

    val numberDisplayFormat by enum("number_display_format", CustomScoreboardRenderer.NumberDisplayFormat.TEXT_COLOR_NUMBER) {
        this.translation = "customscoreboard.config.number_display_format"
    }

    val numberFormat by enum("number_format", NumberFormatType.LONG) {
        this.translation = "customscoreboard.config.number_format"
    }

    val verticalAlignment by enum("vertical_alignment", VerticalAlignment.CENTER) {
        this.translation = "customscoreboard.config.vertical_alignment"
    }

    val horizontalAlignment by enum("horizontal_alignment", HorizontalAlignment.RIGHT) {
        this.translation = "customscoreboard.config.horizontal_alignment"
    }

    val hideWhenTab by boolean(false) {
        this.translation = "customscoreboard.config.hide_when_tab"
    }

    val hideWhenChat by boolean(false) {
        this.translation = "customscoreboard.config.hide_when_chat"
    }

    val hideHypixelScoreboard by boolean("hide_hypixel", true) {
        this.translation = "customscoreboard.config.hide_hypixel"
    }

    val textShadow by boolean("text_shadow", true) {
        this.translation = "customscoreboard.config.text_shadow"
    }

    val customLines by boolean(true) {
        this.translation = "customscoreboard.config.custom_lines"
    }

    val outsideSkyBlock by boolean(false) {
        this.translation = "customscoreboard.config.outside_skyblock"
    }

    val showActiveOnly by boolean(false) {
        this.translation = "customscoreboard.config.show_active_only"
    }

    val updateNotification by boolean("update_notification", true) {
        this.translation = "customscoreboard.config.update_notification"
    }

    val scoreboardOverhaul by boolean(false) {
        this.translation = "customscoreboard.config.scoreboard_overhaul"
    }
}
