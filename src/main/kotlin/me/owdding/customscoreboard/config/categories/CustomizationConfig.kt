package me.owdding.customscoreboard.config.categories

import com.google.gson.JsonElement
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.config.CUSTOM_DRAGGABLE_RENDERER
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toBaseElements
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toConfigStrings
import me.owdding.customscoreboard.feature.SkyHanniOption.shMapper
import me.owdding.customscoreboard.feature.SkyHanniOption.shPath
import me.owdding.customscoreboard.feature.customscoreboard.ChunkedStat
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
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementSowdust
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTime
import me.owdding.customscoreboard.feature.customscoreboard.elements.ElementTitle
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.Utils.observable
import me.owdding.customscoreboard.utils.Utils.updateDisplay
import me.owdding.customscoreboard.utils.Utils.updateIslandCache
import me.owdding.customscoreboard.utils.rendering.alignment.HorizontalAlignment
import me.owdding.customscoreboard.utils.rendering.alignment.VerticalAlignment
import me.owdding.lib.displays.Alignment
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.utils.extentions.valueOfOrNull

object CustomizationConfig : CategoryKt("customization") {
    override val name = Literal("Layout & Appearance")
    private val translationPath = "customscoreboard.config.customization"

    private val default = listOf(
        ElementTitle, ElementLobby, ElementSeparator, ElementDate, ElementTime,
        ElementIsland, ElementArea, ElementProfile, ElementSeparator, ElementPurse,
        ElementMotes, ElementBank, ElementBits, ElementCopper, ElementSowdust,
        ElementGems, ElementHeat, ElementCold, ElementNorthStars, ElementSoulflow,
        ElementSeparator, ElementObjective, ElementSlayer, ElementQuiver, ElementEvents,
        ElementPowder, ElementMayor, ElementParty, ElementPet, ElementFooter,
    ).map { it.id }

    init {
        separator { this.title = "$translationPath.sections.structure" }
    }

    val appearance by transform(
        strings(*default.toTypedArray()) {
            this.translation = "$translationPath.appearance"
            this.renderer = CUSTOM_DRAGGABLE_RENDERER
            this.shPath = "scoreboardEntries"
            shMapper = { json: JsonElement ->
                json.asJsonArray.mapNotNull {
                    when (val string = it.asString) {
                        "EMPTY_LINE" -> ElementSeparator.id
                        "COOKIE" -> ElementCookieBuff.id
                        "SKYBLOCK_XP" -> ElementSkyblockLevel.id
                        "PLAYER_AMOUNT" -> ElementPlayerCount.id
                        "LOBBY_CODE" -> ElementLobby.id
                        "LOCATION" -> ElementArea.id
                        else -> string
                    }
                }
            }
        },
        { it.toConfigStrings() },
        { it.asList().toBaseElements() },
    ).updateIslandCache()

    val events by draggable(*ScoreboardEventEntry.entries.toTypedArray()) {
        this.translation = "$translationPath.events"
        this.shPath = "display.events.eventEntries"
        this.shMapper = { json: JsonElement ->
            json.asJsonArray.mapNotNull { line ->
                val name = line.asString
                val changes = mapOf("SERVER_CLOSE" to ScoreboardEventEntry.SERVER_RESTART, "MINING_EVENTS" to ScoreboardEventEntry.MINING)
                changes[name] ?: ScoreboardEventEntry.entries.find { it.name == name }
            }
        }
    }.updateIslandCache()

    init {
        separator { this.title = "$translationPath.sections.tablist" }
    }

    val tablistLines by draggable<TabWidget> {
        this.translation = "$translationPath.tablist_lines"
    }.observable { _, _ -> TabWidgetHelper.updateTablistLineCache() }

    init {
        separator { this.title = "$translationPath.sections.chunked" }
    }

    val chunkedStats by draggable(*ChunkedStat.entries.toTypedArray()) {
        this.translation = "$translationPath.chunked_stats"
        this.shPath = "display.chunkedStats.chunkedStats"
        this.shMapper = { json: JsonElement ->
            json.asJsonArray.mapNotNull { line -> ChunkedStat.entries.find { stat -> stat.name == line.asString } }
        }
    }.updateIslandCache()

    val statsPerLine by int(3) {
        this.translation = "$translationPath.chunked_stats_per_line"
        this.range = 1..5
        this.shPath = "display.chunkedStats.maxStatsPerLine"
    }

    init {
        separator { this.title = "$translationPath.sections.title" }
    }

    val useHypixelTitle by boolean(true) {
        this.translation = "$translationPath.use_hypixel_title"
        this.shPath = "display.titleAndFooter.useCustomTitle"
        this.shMapper = { !it.asBoolean }
    }

    val titleAlignment by enum(Alignment.CENTER) {
        this.translation = "$translationPath.title_alignment"
        this.shPath = "display.titleAndFooter.alignTitle"
        this.shMapper = { valueOfOrNull<Alignment>(it.asString) ?: Alignment.CENTER }
    }

    val titleUseCustomText by boolean(false) {
        this.translation = "$translationPath.title_use_custom_text"
        this.shPath = "display.titleAndFooter.useCustomTitle"
    }

    val titleText by string("") {
        this.translation = "$translationPath.title_custom_text"
        this.shPath = "display.titleAndFooter.customTitle"
    }

    init {
        separator { this.title = "$translationPath.sections.footer" }
    }

    val footerAlignment by enum(Alignment.CENTER) {
        this.translation = "$translationPath.footer_alignment"
        this.shPath = "display.titleAndFooter.alignFooter"
        this.shMapper = { valueOfOrNull<Alignment>(it.asString) ?: Alignment.CENTER }
    }

    val footerUseCustomText by boolean(false) {
        this.translation = "$translationPath.footer_use_custom_text"
        this.shPath = "display.titleAndFooter.useCustomFooter"
    }

    val footerText by string("") {
        this.translation = "$translationPath.footer_custom_text"
        this.shPath = "display.titleAndFooter.customFooter"
    }

    init {
        separator { this.title = "$translationPath.sections.layout" }
    }

    val scale by double(1.0) {
        this.translation = "$translationPath.scale"
        this.range = 0.1..2.0
        this.slider = true
    }

    val lineSpacing by int(0) {
        this.translation = "$translationPath.line_spacing"
        this.range = 0..10
        this.slider = true
        this.shPath = "display.lineSpacing"
        this.shMapper = { (it.asInt - 10).coerceAtLeast(0) }
    }.updateDisplay()

    val verticalAlignment by enum("vertical_alignment", VerticalAlignment.CENTER) {
        this.translation = "$translationPath.vertical_alignment"
        this.shPath = "display.alignment.verticalAlignment"
        this.shMapper = { valueOfOrNull<VerticalAlignment>(it.asString) ?: VerticalAlignment.CENTER }
    }

    val horizontalAlignment by enum("horizontal_alignment", HorizontalAlignment.RIGHT) {
        this.translation = "$translationPath.horizontal_alignment"
        this.shPath = "display.alignment.horizontalAlignment"
        this.shMapper = { valueOfOrNull<HorizontalAlignment>(it.asString) ?: HorizontalAlignment.RIGHT }
    }

    val defaultTextAlignment by enum(Alignment.START) {
        this.translation = "$translationPath.default_text_alignment"
        this.shPath = "display.textAlignment"
        this.shMapper = {
            when (it.asString) {
                "LEFT" -> Alignment.START
                "CENTER" -> Alignment.CENTER
                "RIGHT" -> Alignment.END
                else -> Alignment.START
            }
        }
    }
}
