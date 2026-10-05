package me.owdding.customscoreboard.config.category

import com.google.gson.JsonElement
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.compat.SkyHanniOption.shMapper
import me.owdding.customscoreboard.compat.SkyHanniOption.shPath
import me.owdding.customscoreboard.config.CUSTOM_DRAGGABLE_RENDERER
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toBaseElements
import me.owdding.customscoreboard.config.CustomDraggableList.Companion.toConfigStrings
import me.owdding.customscoreboard.core.ChunkedStat
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
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
import me.owdding.lib.overlays.ConfigPosition
import me.owdding.lib.utils.config.cachedTransformPlaceholderComponents
import me.owdding.lib.utils.config.transform
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.utils.extentions.valueOfOrNull

object CustomizationConfig : CategoryKt("customization") {
    override val name = Literal("Layout & Appearance")
    override val baseTranslation: String = "customscoreboard.config.customization"

    private val default = listOf(
        TitleElement, LobbyElement, SeparatorElement, DateElement, TimeElement,
        IslandElement, AreaElement, ProfileElement, SeparatorElement, PurseElement,
        MotesElement, BankElement, BitsElement, CopperElement, SowdustElement, KernelsElement,
        GemsElement, HeatElement, ColdElement, NorthStarsElement, SoulflowElement,
        SeparatorElement, ObjectiveElement, SlayerElement, QuiverElement, EventsElement,
        PowderElement, MayorElement, PartyElement, PetElement, FooterElement,
    ).map { it.id }

    init {
        separator { this.title = "sections.structure" }
    }

    var appearance by transform(
        strings(*default.toTypedArray()) {
            this.translation = "appearance"
            this.renderer = CUSTOM_DRAGGABLE_RENDERER
            this.shPath = "scoreboardEntries"
            shMapper = { json: JsonElement ->
                val list = json.asJsonArray.mapNotNull {
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
                }.toMutableList()

                val copperIndex = list.indexOf(CopperElement.id)
                if (copperIndex != -1 && !list.contains(KernelsElement.id)) {
                    list.add(copperIndex + 1, KernelsElement.id)
                }

                list
            }
        },
        { it.toConfigStrings() },
        { it.asList().toBaseElements() },
    ).updateIslandCache()

    var events by draggable(*ScoreboardEventEntry.entries.filter { it != ScoreboardEventEntry.STARTING_SOON_TABLIST }.toTypedArray()) {
        this.translation = "events"
        this.shPath = "display.events.eventEntries"
        this.shMapper = { json: JsonElement ->
            json.asJsonArray.mapNotNull { line ->
                val name = line.asString
                val changes = mapOf(
                    "SERVER_CLOSE" to ScoreboardEventEntry.SERVER_RESTART,
                    "MINING_EVENTS" to ScoreboardEventEntry.MINING,
                    "GALATEA" to ScoreboardEventEntry.FORAGING,
                    "ACTIVE_TABLIST_EVENTS" to ScoreboardEventEntry.ACTIVE_TABLIST,
                    "STARTING_SOON_TABLIST_EVENTS" to ScoreboardEventEntry.STARTING_SOON_TABLIST,
                    "JACOB_CONTEST" to ScoreboardEventEntry.JACOBS_CONTEST,
                )
                changes[name] ?: ScoreboardEventEntry.entries.find { it.name == name }
            }
        }
    }.updateIslandCache()

    init {
        separator { this.title = "sections.tablist" }
    }

    val tablistLines by draggable<TabWidget> {
        this.translation = "tablist_lines"
    }.observable { _, _ -> TabWidgetHelper.updateTablistLineCache() }

    init {
        separator { this.title = "sections.chunked" }
    }

    val chunkedStats by draggable(*ChunkedStat.entries.toTypedArray()) {
        this.translation = "chunked_stats"
        this.shPath = "display.chunkedStats.chunkedStats"
        this.shMapper = { json: JsonElement ->
            json.asJsonArray.mapNotNull { line -> ChunkedStat.entries.find { stat -> stat.name == line.asString } }
        }
    }.updateIslandCache()

    val statsPerLine by int(3) {
        this.translation = "chunked_stats_per_line"
        this.range = 1..5
        this.shPath = "display.chunkedStats.maxStatsPerLine"
    }

    init {
        separator {
            this.title = "sections.title"
            this.description = "sections.title.desc"
        }
    }

    val useHypixelTitle by boolean(true) {
        this.translation = "use_hypixel_title"
        this.shPath = "display.titleAndFooter.useCustomTitle"
        this.shMapper = { !it.asBoolean }
    }

    val titleAlignment by enum(Alignment.CENTER) {
        this.translation = "title_alignment"
        this.shPath = "display.titleAndFooter.alignTitle"
        this.shMapper = { valueOfOrNull<Alignment>(it.asString) ?: Alignment.CENTER }
    }

    val titleUseCustomText by boolean(false) {
        this.translation = "title_use_custom_text"
        this.shPath = "display.titleAndFooter.useCustomTitle"
    }

    val useCustomTitleOutsideSkyBlock by boolean(false) {
        this.translation = "use_custom_title_outside_skyblock"
        this.shPath = "display.titleAndFooter.useCustomTitleOutsideSkyBlock"
    }

    val titleText by strings("") {
        this.translation = "title_custom_text"
        this.shPath = "display.titleAndFooter.customTitle"
        this.shMapper = { it.asString.lines().map(::convertLegacyToPlaceholder).toTypedArray() }
    }.cachedTransformPlaceholderComponents()

    init {
        separator { this.title = "sections.footer" }
    }

    val footerAlignment by enum(Alignment.CENTER) {
        this.translation = "footer_alignment"
        this.shPath = "display.titleAndFooter.alignFooter"
        this.shMapper = { valueOfOrNull<Alignment>(it.asString) ?: Alignment.CENTER }
    }

    val footerUseCustomText by boolean(false) {
        this.translation = "footer_use_custom_text"
        this.shPath = "display.titleAndFooter.useCustomFooter"
    }

    val footerText by strings("") {
        this.translation = "footer_custom_text"
        this.shPath = "display.titleAndFooter.customFooter"
        this.shMapper = { it.asString.lines().map(::convertLegacyToPlaceholder).toTypedArray() }
    }.cachedTransformPlaceholderComponents()

    val alphaFooterText by strings("") {
        this.translation = "custom_alpha_footer"
        this.shPath = "display.titleAndFooter.customAlphaFooter"
        this.shMapper = { it.asString.lines().map(::convertLegacyToPlaceholder).toTypedArray() }
    }.cachedTransformPlaceholderComponents()

    init {
        separator { this.title = "sections.layout" }
    }

    val scale by double(1.0) {
        this.translation = "scale"
        this.range = 0.1..2.0
        this.slider = true
    }

    val lineSpacing by int(0) {
        this.translation = "line_spacing"
        this.range = 0..10
        this.slider = true
        this.shPath = "display.lineSpacing"
        this.shMapper = { (it.asInt - 10).coerceAtLeast(0) }
    }.updateDisplay()

    val verticalAlignment by enum("vertical_alignment", VerticalAlignment.CENTER) {
        this.translation = "vertical_alignment"
        this.shPath = "display.alignment.verticalAlignment"
        this.shMapper = { valueOfOrNull<VerticalAlignment>(it.asString) ?: VerticalAlignment.CENTER }
    }

    val horizontalAlignment by enum("horizontal_alignment", HorizontalAlignment.RIGHT) {
        this.translation = "horizontal_alignment"
        this.shPath = "display.alignment.horizontalAlignment"
        this.shMapper = { valueOfOrNull<HorizontalAlignment>(it.asString) ?: HorizontalAlignment.RIGHT }
    }

    val position by obj(ConfigPosition(0, 0)) {
        condition = { false }
    }

    val defaultTextAlignment by enum(Alignment.START) {
        this.translation = "default_text_alignment"
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

    init {
        separator { this.title = "sections.presets" }

        button {
            this.title = "preset.skyblock"
            this.description = "preset.skyblock.desc"
            this.text = "preset.skyblock.text"
            onClick {
                appearance = listOf(
                    TitleElement, LobbyElement, SeparatorElement, DateElement, TimeElement,
                    AreaElement, SeparatorElement, PurseElement, MotesElement, BitsElement,
                    CopperElement, SowdustElement, KernelsElement, HeatElement, ColdElement,
                    NorthStarsElement, SeparatorElement, ObjectiveElement, SlayerElement,
                    EventsElement, FooterElement,
                )
                events = ScoreboardEventEntry.entries.filter { it != ScoreboardEventEntry.STARTING_SOON_TABLIST }.toTypedArray()

                CustomScoreboardMod.config.save()
                CustomScoreboardRenderer.updateIslandCache()
                CustomScoreboardRenderer.updateDisplay()
            }
        }

        separator {
            this.title = "sections.custom_prefixes"
            this.description = "sections.custom_prefixes.desc"
        }
    }

    val enableCustomPrefixes by boolean(false) {
        this.translation = "custom_prefix.enabled"
    }

    private fun prefix(prefix: CustomPrefix) = strings(prefix.default) {
        this.name = TranslatableValue("${prefix.default.removeSuffix(":")} Prefix")
    }.cachedTransformPlaceholderComponents().transform({ listOf(it) }, { it.first() })

    val pursePrefix by prefix(CustomPrefix.PURSE)
    val piggyPrefix by prefix(CustomPrefix.PIGGY)
    val bankPrefix by prefix(CustomPrefix.BANK)
    val motesPrefix by prefix(CustomPrefix.MOTES)
    val bitsPrefix by prefix(CustomPrefix.BITS)
    val copperPrefix by prefix(CustomPrefix.COPPER)
    val sowdustPrefix by prefix(CustomPrefix.SOWDUST)
    val kernelsPrefix by prefix(CustomPrefix.KERNELS)
    val heatPrefix by prefix(CustomPrefix.HEAT)
    val coldPrefix by prefix(CustomPrefix.COLD)
    val northStarsPrefix by prefix(CustomPrefix.NORTH_STARS)
    val soulflowPrefix by prefix(CustomPrefix.SOULFLOW)
    val gemsPrefix by prefix(CustomPrefix.GEMS)
    val cookieBuffPrefix by prefix(CustomPrefix.COOKIE_BUFF)
    val petPrefix by prefix(CustomPrefix.PET)
    val godPotionPrefix by prefix(CustomPrefix.GOD_POTION)
    val maxwellPowerPrefix by prefix(CustomPrefix.MAXWELL_POWER)
    val maxwellTuningPrefix by prefix(CustomPrefix.MAXWELL_TUNING)
    val playerCountPrefix by prefix(CustomPrefix.PLAYER_COUNT)
}

enum class CustomPrefix(val default: String, private val _prefix: () -> Component) {
    PURSE("Purse:", CustomizationConfig::pursePrefix),
    PIGGY("Piggy:", CustomizationConfig::piggyPrefix),
    BANK("Bank:", CustomizationConfig::bankPrefix),
    MOTES("Motes:", CustomizationConfig::motesPrefix),
    BITS("Bits:", CustomizationConfig::bitsPrefix),
    COPPER("Copper:", CustomizationConfig::copperPrefix),
    SOWDUST("Sowdust:", CustomizationConfig::sowdustPrefix),
    KERNELS("Kernels:", CustomizationConfig::kernelsPrefix),
    HEAT("Heat:", CustomizationConfig::heatPrefix),
    COLD("Cold:", CustomizationConfig::coldPrefix),
    NORTH_STARS("North Stars:", CustomizationConfig::northStarsPrefix),
    SOULFLOW("Soulflow:", CustomizationConfig::soulflowPrefix),
    GEMS("Gems:", CustomizationConfig::gemsPrefix),
    COOKIE_BUFF("Cookie Buff:", CustomizationConfig::cookieBuffPrefix),
    PET("Pet:", CustomizationConfig::petPrefix),
    GOD_POTION("God Potion:", CustomizationConfig::godPotionPrefix),
    MAXWELL_POWER("Power:", CustomizationConfig::maxwellPowerPrefix),
    MAXWELL_TUNING("Tunings:", CustomizationConfig::maxwellTuningPrefix),
    PLAYER_COUNT("Players:", CustomizationConfig::playerCountPrefix),
    ;

    val prefix get() = _prefix()
}
