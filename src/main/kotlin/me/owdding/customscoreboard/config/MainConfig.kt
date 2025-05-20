package me.owdding.customscoreboard.config

import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.config.objects.TitleOrFooterObject
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import me.owdding.customscoreboard.generated.ScoreboardEntry
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.NumberFormatType
import me.owdding.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import me.owdding.customscoreboard.utils.rendering.alignment.VerticalAlignment

object MainConfig : ConfigKt("customscoreboard/config") {

    override val name get() = TranslatableValue("Custom Scoreboard Config")
    override val description get() = TranslatableValue("by j10a1n15. Version ${Main.VERSION}")
    override val links: Array<ResourcefulConfigLink>
        get() = arrayOf(
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

    init {
        category(BackgroundConfig)
        category(LinesConfig)
    }

    var enabled by boolean(true) {
        this.translation = "config.cs.enabled"
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
            this.translation = "config.cs.appearance"
            this.duplicatable = listOf(ScoreboardEntry.SEPARATOR).toTypedArray()
        },
    ) { old, new ->
        CustomScoreboardRenderer.updateIslandCache()
    }

    val events by observable(
        draggable(*ScoreboardEventEntry.entries.toTypedArray()) {
            this.translation = "config.cs.events"
        },
    ) { old, new ->
        CustomScoreboardRenderer.updateIslandCache()
    }

    val title = obj("title_options", TitleOrFooterObject()) {
        this.translation = "config.cs.title_options"
    }

    val footer = obj("footer_options", TitleOrFooterObject()) {
        this.translation = "config.cs.footer_options"
    }

    val numberDisplayFormat by enum("number_display_format", CustomScoreboardRenderer.NumberDisplayFormat.TEXT_COLOR_NUMBER) {
        this.translation = "config.cs.number_display_format"
    }

    val numberFormat by enum("number_format", NumberFormatType.LONG) {
        this.translation = "config.cs.number_format"
    }

    val verticalAlignment by enum("vertical_alignment", VerticalAlignment.CENTER) {
        this.translation = "config.cs.vertical_alignment"
    }

    val horizontalAlignment by enum("horizontal_alignment", HorizontalAlignment.RIGHT) {
        this.translation = "config.cs.horizontal_alignment"
    }

    val hideHypixelScoreboard by boolean("hide_hypixel", true) {
        this.translation = "config.cs.hide_hypixel"
    }

    val textShadow by boolean("text_shadow", true) {
        this.translation = "config.cs.text_shadow"
    }

    val updateNotification by boolean("update_notification", true) {
        this.translation = "config.cs.update_notification"
    }

}
