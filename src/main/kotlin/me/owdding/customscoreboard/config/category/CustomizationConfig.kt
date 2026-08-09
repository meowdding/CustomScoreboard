package me.owdding.customscoreboard.config.category

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.compat.SkyHanniOption.shMapper
import me.owdding.customscoreboard.compat.SkyHanniOption.shPath
import me.owdding.customscoreboard.config.CUSTOM_DRAGGABLE_RENDERER
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toBaseElements
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toConfigStrings
import me.owdding.customscoreboard.core.ChunkedStat
import me.owdding.customscoreboard.core.TabWidgetHelper
import me.owdding.customscoreboard.elements.AreaElement
import me.owdding.customscoreboard.elements.BankElement
import me.owdding.customscoreboard.elements.BitsElement
import me.owdding.customscoreboard.elements.ColdElement
import me.owdding.customscoreboard.elements.CookieBuffElement
import me.owdding.customscoreboard.elements.CopperElement
import me.owdding.customscoreboard.elements.DateElement
import me.owdding.customscoreboard.elements.ElementMaxwellPower
import me.owdding.customscoreboard.elements.EventsElement
import me.owdding.customscoreboard.elements.FooterElement
import me.owdding.customscoreboard.elements.GemsElement
import me.owdding.customscoreboard.elements.HeatElement
import me.owdding.customscoreboard.elements.IslandElement
import me.owdding.customscoreboard.elements.KernelsElement
import me.owdding.customscoreboard.elements.LobbyElement
import me.owdding.customscoreboard.elements.MaxwellTuningsElement
import me.owdding.customscoreboard.elements.MayorElement
import me.owdding.customscoreboard.elements.MotesElement
import me.owdding.customscoreboard.elements.NorthStarsElement
import me.owdding.customscoreboard.elements.ObjectiveElement
import me.owdding.customscoreboard.elements.PartyElement
import me.owdding.customscoreboard.elements.PetElement
import me.owdding.customscoreboard.elements.PlayerCountElement
import me.owdding.customscoreboard.elements.PowderElement
import me.owdding.customscoreboard.elements.ProfileElement
import me.owdding.customscoreboard.elements.PurseElement
import me.owdding.customscoreboard.elements.QuiverElement
import me.owdding.customscoreboard.elements.SeparatorElement
import me.owdding.customscoreboard.elements.SkyblockLevelElement
import me.owdding.customscoreboard.elements.SlayerElement
import me.owdding.customscoreboard.elements.SoulflowElement
import me.owdding.customscoreboard.elements.SowdustElement
import me.owdding.customscoreboard.elements.TimeElement
import me.owdding.customscoreboard.elements.TitleElement
import me.owdding.customscoreboard.generated.ScoreboardEventEntry
import me.owdding.customscoreboard.utils.Utils.convertLegacyToPlaceholder
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
        TitleElement, LobbyElement, SeparatorElement, DateElement, TimeElement,
        IslandElement, AreaElement, ProfileElement, SeparatorElement, PurseElement,
        MotesElement, BankElement, BitsElement, CopperElement, SowdustElement, KernelsElement,
        GemsElement, HeatElement, ColdElement, NorthStarsElement, SoulflowElement,
        SeparatorElement, ObjectiveElement, SlayerElement, QuiverElement, EventsElement,
        PowderElement, MayorElement, PartyElement, PetElement, FooterElement,
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
                        "COOKIE" -> CookieBuffElement.id
                        "SKYBLOCK_XP" -> SkyblockLevelElement.id
                        "PLAYER_AMOUNT" -> PlayerCountElement.id
                        "LOBBY_CODE" -> LobbyElement.id
                        "LOCATION" -> AreaElement.id
                        "POWER" -> ElementMaxwellPower.id
                        "TUNING" -> MaxwellTuningsElement.id
                        else if string.startsWith("EMPTY_LINE") -> SeparatorElement.id
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
                val changes = mapOf(
                    "SERVER_CLOSE" to ScoreboardEventEntry.SERVER_RESTART,
                    "MINING_EVENTS" to ScoreboardEventEntry.MINING,
                    "GALATEA" to ScoreboardEventEntry.FORAGING,
                )
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
        separator {
            this.title = "$translationPath.sections.title"
            this.description = "$translationPath.sections.title.desc"
        }
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

    val useCustomTitleOutsideSkyBlock by boolean(false) {
        this.translation = "$translationPath.use_custom_title_outside_skyblock"
        this.shPath = "display.titleAndFooter.useCustomTitleOutsideSkyBlock"
    }

    val titleText by strings("") {
        this.translation = "$translationPath.title_custom_text"
        this.shPath = "display.titleAndFooter.customTitle"
        this.shMapper = { JsonArray().apply { it.asString.lines().map(::convertLegacyToPlaceholder).forEach(::add) } }
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

    val footerText by strings("") {
        this.translation = "$translationPath.footer_custom_text"
        this.shPath = "display.titleAndFooter.customFooter"
        this.shMapper = { JsonArray().apply { it.asString.lines().map(::convertLegacyToPlaceholder).forEach(::add) } }
    }

    val alphaFooterText by strings("") {
        this.translation = "$translationPath.custom_alpha_footer"
        this.shPath = "display.titleAndFooter.customAlphaFooter"
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
