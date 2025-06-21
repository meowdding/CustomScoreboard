package me.owdding.customscoreboard.config

import com.google.gson.JsonObject
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.config.objects.TitleOrFooterObject
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementMayor
import me.owdding.customscoreboard.generated.ScoreboardEntry
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.NumberFormatType
import me.owdding.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import me.owdding.customscoreboard.utils.rendering.alignment.VerticalAlignment
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

            json
        }
    )
    //endregion

    init {
        category(BackgroundConfig)
        category(LinesConfig)
    }

    var enabled by boolean(true) {
        this.translation = "customscoreboard.config.enabled"
    }

    private val default = listOf<ScoreboardEntry>(
        ScoreboardEntry.TITLE,
        ScoreboardEntry.LOBBY,
        ScoreboardEntry.SEPARATOR,
        ScoreboardEntry.DATE,
        ScoreboardEntry.TIME,
        ScoreboardEntry.ISLAND,
        ScoreboardEntry.AREA,
        ScoreboardEntry.PROFILE,
        ScoreboardEntry.SEPARATOR,
        ScoreboardEntry.PURSE,
        ScoreboardEntry.MOTES,
        ScoreboardEntry.BANK,
        ScoreboardEntry.BITS,
        ScoreboardEntry.COPPER,
        ScoreboardEntry.GEMS,
        ScoreboardEntry.HEAT,
        ScoreboardEntry.COLD,
        ScoreboardEntry.NORTH_STARS,
        ScoreboardEntry.SOULFLOW,
        ScoreboardEntry.SEPARATOR,
        ScoreboardEntry.OBJECTIVE,
        ScoreboardEntry.SLAYER,
        ScoreboardEntry.QUIVER,
        ScoreboardEntry.EVENTS,
        ScoreboardEntry.POWDER,
        ScoreboardEntry.MAYOR,
        ScoreboardEntry.PARTY,
        ScoreboardEntry.PET,
        ScoreboardEntry.FOOTER,
    )

    val appearance by observable(
        draggable(*default.toTypedArray()) {
            this.translation = "customscoreboard.config.appearance"
            this.duplicatable = listOf(ScoreboardEntry.SEPARATOR).toTypedArray()
        },
    ) { old, new ->
        CustomScoreboardRenderer.updateIslandCache()
    }

    val events by observable(
        draggable(*ScoreboardEventEntry.entries.toTypedArray()) {
            this.translation = "customscoreboard.config.events"
        },
    ) { old, new ->
        CustomScoreboardRenderer.updateIslandCache()
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

    val updateNotification by boolean("update_notification", true) {
        this.translation = "customscoreboard.config.update_notification"
    }

}
