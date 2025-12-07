package me.owdding.customscoreboard.config

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toBaseElements
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toConfigStrings
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.config.objects.TitleOrFooterObject
import me.owdding.customscoreboard.feature.ConfigTransfer
import me.owdding.customscoreboard.feature.ShTransferableConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.TabWidgetHelper
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementArea
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBank
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementBits
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCold
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementCookieBuff
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
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPlayerCount
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPowder
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementProfile
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementPurse
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementQuiver
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSeparator
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSkyblockLevel
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSlayer
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSoulflow
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTime
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTitle
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.NumberFormatType
import me.owdding.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import me.owdding.customscoreboard.utils.rendering.alignment.VerticalAlignment
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import java.util.function.UnaryOperator

object MainConfig : ShTransferableConfig("customscoreboard/config") {

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

    override val version = 3

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
                shPath = "scoreboardEntries"
                shMapper = { json: JsonElement ->
                    json.asJsonArray.mapNotNull {
                        when (val string = it.asString) {
                            "EMPTY_LINE" -> ElementSeparator.id
                            "COOKIE" -> ElementCookieBuff.id
                            "SKYBLOCK_XP" -> ElementSkyblockLevel.id
                            "PLAYER_AMOUNT" -> ElementPlayerCount.id
                            "LOBBY_CODE" -> ElementLobby.id
                            "LOCATION" -> ElementArea.id
                            "EXTRA" -> null
                            "VISITING" -> null
                            else -> string
                        }
                    }
                }
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
            shPath = "display.events.eventEntries"
            shMapper = { json: JsonElement ->
                json.asJsonArray.mapNotNull {
                    val name = it.asString
                    val changes = mapOf(
                        "SERVER_CLOSE" to ScoreboardEventEntry.SERVER_RESTART,
                        "MINING_EVENTS" to ScoreboardEventEntry.MINING,
                        "ACTIVE_TABLIST_EVENTS" to null,
                        "STARTING_SOON_TABLIST_EVENTS" to null,
                    )
                    changes[name] ?: ScoreboardEventEntry.entries.find { it.name == name }
                }
            }
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
        this.shPath = "display.titleAndFooter"
        this.shMapper = { json: JsonElement ->
            val obj = json.asJsonObject
            JsonObject().apply {
                addProperty("alignment", obj.get("alignTitle").asString)
                addProperty("useCustomText", obj.get("useCustomTitle").asBoolean)
                addProperty("text", obj.get("customTitle").asString)
            }
        }
    }

    val footer = obj("footer_options", TitleOrFooterObject()) {
        this.translation = "customscoreboard.config.footer_options"
        this.shPath = "display.titleAndFooter"
        this.shMapper = { json: JsonElement ->
            val obj = json.asJsonObject
            JsonObject().apply {
                addProperty("alignment", obj.get("alignFooter").asString)
                addProperty("useCustomText", obj.get("useCustomFooter").asBoolean)
                addProperty("text", obj.get("customFooter").asString)
            }
        }
    }

    val numberDisplayFormat by enum("number_display_format", CustomScoreboardRenderer.NumberDisplayFormat.TEXT_COLOR_NUMBER) {
        this.translation = "customscoreboard.config.number_display_format"
        this.shPath = "display.numberDisplayFormat"
    }

    val numberFormat by enum("number_format", NumberFormatType.LONG) {
        this.translation = "customscoreboard.config.number_format"
        this.shPath = "display.numberFormat"
    }

    val verticalAlignment by enum("vertical_alignment", VerticalAlignment.CENTER) {
        this.translation = "customscoreboard.config.vertical_alignment"
        this.shPath = "display.alignment.verticalAlignment"
    }

    val horizontalAlignment by enum("horizontal_alignment", HorizontalAlignment.RIGHT) {
        this.translation = "customscoreboard.config.horizontal_alignment"
        this.shPath = "display.alignment.horizontalAlignment"
    }

    val hideWhenTab by boolean(false) {
        this.translation = "customscoreboard.config.hide_when_tab"
    }

    val hideWhenChat by boolean(false) {
        this.translation = "customscoreboard.config.hide_when_chat"
    }

    val hideHypixelScoreboard by boolean("hide_hypixel", true) {
        this.translation = "customscoreboard.config.hide_hypixel"
        this.shPath = "display.hideVanillaScoreboard"
    }

    val textShadow by boolean("text_shadow", true) {
        this.translation = "customscoreboard.config.text_shadow"
    }

    val customLines by boolean(true) {
        this.translation = "customscoreboard.config.custom_lines"
        this.shPath = "display.useCustomLines"
    }

    val outsideSkyBlock by boolean(false) {
        this.translation = "customscoreboard.config.outside_skyblock"
    }

    val updateNotification by boolean("update_notification", true) {
        this.translation = "customscoreboard.config.update_notification"
    }

    val scoreboardOverhaul by boolean(false) {
        this.translation = "customscoreboard.config.scoreboard_overhaul"
    }

    init {
        button {
            this.onClick {
                ConfigTransfer.transfer()
            }
        }
    }
}
