package me.owdding.customscoreboard.core

import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.config.Config
import me.owdding.customscoreboard.elements.ObjectiveElement
import me.owdding.customscoreboard.elements.SlayerElement
import me.owdding.customscoreboard.events.DarkAuctionEvent
import me.owdding.customscoreboard.events.ForagingEvent
import me.owdding.customscoreboard.events.JacobsContestEvent
import me.owdding.customscoreboard.events.TrapperEvent
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.Utils.nextAfter
import me.owdding.customscoreboard.utils.Utils.sendWithPrefix
import me.owdding.ktmodules.Module
import me.owdding.lib.extensions.removeIf
import me.owdding.lib.utils.strings.StringGroup.Companion.resolve
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.OnlyOnSkyBlock
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.currentInstant
import tech.thatgravyboat.skyblockapi.utils.extentions.since
import tech.thatgravyboat.skyblockapi.utils.json.Json.toJson
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.clipboard
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.url
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Module
object UnknownLinesHandler {

    val brokenLinesRegexes by RemoteStrings.resolve().regexes()

    private val allUnknownLines = mutableMapOf<String, Pair<Component, Instant>>()
    private var lastScoreboard = emptyList<Component>()

    @Subscription
    @OnlyOnSkyBlock
    private fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        lastScoreboard = event.newComponents
        val patternsToExclude = CustomScoreboardRenderer.activeRegexes
        val unknownLines: MutableMap<String, Component> = event.newComponents.filter { it.stripped.isNotBlank() }.associateBy { it.stripped }
            .filterNotTo(mutableMapOf()) { line ->
                patternsToExclude.any { pattern -> pattern.matches(line.key) }
            }


        removeSidebarSection(
            unknownLines,
            event.new,
            ObjectiveElement.objectiveTitleRegex,
            2,
        )

        removeSidebarSection(
            unknownLines,
            event.new,
            JacobsContestEvent.contestRegex.regex(),
            3,
        )

        removeSidebarSection(
            unknownLines,
            event.new,
            ForagingEvent.contestRegex.regex(),
            2,
        )

        removeSidebarSection(
            unknownLines,
            event.new,
            SlayerElement.slayerQuestRegex.regex(),
            2,
        )

        removeSidebarSection(
            unknownLines,
            event.new,
            TrapperEvent.mobLocationRegex.regex(),
        )

        removeSidebarSection(
            unknownLines,
            event.new,
            DarkAuctionEvent.currentItemRegex.regex(),
        )

        allUnknownLines.removeIf { (key, value) -> key !in unknownLines && value.second.since() >= 5.seconds } // line was removed from scoreboard
        if (unknownLines.isEmpty()) return

        unknownLines.forEach { (key, value) ->
            if (!allUnknownLines.containsKey(key)) {
                warn(value)
            }
            allUnknownLines[key] = value to currentInstant()
        }
    }

    val unknownLines: List<Component> get() = allUnknownLines.map { (_, value) -> value.first }

    private fun removeSidebarSection(
        unknownLines: MutableMap<String, Component>,
        scoreboard: List<String>,
        regex: Regex,
        linesAfter: Int = 1,
    ) {
        val header = scoreboard.firstOrNull { regex.matches(it) } ?: return

        for (i in 1..linesAfter) {
            val lineToRemove = scoreboard.nextAfter(header, i) ?: continue
            if (lineToRemove.isBlank()) continue
            unknownLines.remove(lineToRemove)
        }
    }

    private fun warn(line: Component) {
        val data = buildString {
            appendLine("Line: ${line.stripped}")
            appendLine("Json: ${line.toJson(ComponentSerialization.CODEC)}")
            appendLine("Island: ${LocationAPI.island}")
            appendLine("Area: ${LocationAPI.area}")
            appendLine("Scoreboard: ${lastScoreboard.toJson(ComponentSerialization.CODEC.listOf())}")
        }

        if (Config.unknownLinesWarning) {
            Text.of("Found unknown line ") {
                color = TextColor.RED

                append(line.stripped, TextColor.GREEN)
                append(".")
                append(" Report this on the Discord.") {
                    color = TextColor.BLUE
                    url = CustomScoreboardMod.DISCORD
                }

                clipboard = data
            }.sendWithPrefix()
        }
        CustomScoreboardMod.warn("Found unknown line:\n$data")
    }
}
