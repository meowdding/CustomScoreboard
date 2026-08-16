package me.owdding.customscoreboard.utils

import com.teamresourceful.resourcefulconfigkt.api.ConfigDelegateProvider
import com.teamresourceful.resourcefulconfigkt.api.ObservableEntry
import com.teamresourceful.resourcefulconfigkt.api.RConfigKtEntry
import me.owdding.customscoreboard.core.CustomScoreboardRenderer
import me.owdding.dfu.item.LegacyTextFixer
import me.owdding.ktmodules.AutoCollect
import me.owdding.lib.rendering.text.serialization.TagComponentSerialization
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.extentions.until
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object Utils {
    fun Duration.toFormatYears(maxUnits: Int = 2): String {
        val years = inWholeDays / 365
        val days = inWholeDays % 365
        val hours = inWholeHours % 24
        val minutes = inWholeMinutes % 60
        val seconds = inWholeSeconds % 60

        val parts = buildList {
            if (years > 0) add("${years}y")
            if (days > 0) add("${days}d")
            if (hours > 0) add("${hours}h")
            if (minutes > 0) add("${minutes}m")
            if (years <= 0 && days <= 0 && seconds > 0) add("${seconds}s")
        }

        return if (parts.isEmpty()) "0s" else parts.take(maxUnits).joinToString(" ")
    }

    fun <T> Collection<T>.nextAfter(element: T, skip: Int = 1): T? {
        val index = indexOfFirst { if (it is Component && element is String) it.stripped == element else it == element }
        if (index == -1 || index + skip >= size) return null
        return elementAt(index + skip)
    }

    inline fun <T, C : MutableCollection<T>> C.replaceWith(builder: C.() -> Unit): C = apply {
        clear()
        builder()
    }

    fun <C : MutableCollection<Component>> C.replaceWithMatches(
        newLines: Collection<Component>,
        regexes: Collection<ComponentRegex>,
    ): C = replaceWith {
        newLines.filterTo(this) { component ->
            regexes.any { it.matches(component) }
        }
    }

    fun <T> List<T>.sublistFromFirst(amount: Int, predicate: (T) -> Boolean): List<T> {
        val index = indexOfFirst(predicate)
        if (index == -1) return emptyList()
        return subList(index, (index + amount).coerceAtMost(size))
    }

    val PREFIX = Text.join(
        Text.of("[").withColor(TextColor.GRAY),
        Text.of("CustomScoreboard").withColor(TextColor.AQUA),
        Text.of("] ").withColor(TextColor.GRAY),
    )

    fun Component.sendWithPrefix() = Text.join(PREFIX, this).send()

    @Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
    inline fun <T> Any?.unsafeCast(): T = this as T

    fun String.moulConfigColor(): Int = split(":").map(String::toInt).let {
        ARGB.color(it[1], it[2], it[3], it[4])
    }

    fun <T> ConfigDelegateProvider<RConfigKtEntry<T>>.observable(onChange: (T, T) -> Unit) = ObservableEntry(this, onChange)

    fun <T> ConfigDelegateProvider<RConfigKtEntry<T>>.updateDisplay() = ObservableEntry(this) { _, _ -> CustomScoreboardRenderer.updateDisplay() }
    fun <T> ConfigDelegateProvider<RConfigKtEntry<T>>.updateIslandCache() = ObservableEntry(this) { _, _ -> CustomScoreboardRenderer.updateIslandCache() }

    fun convertLegacyToPlaceholder(string: String): String {
        val comp = LegacyTextFixer.parse(string.replace("&&", "§"))
        return TagComponentSerialization.serialize(comp)
    }
}


@AutoCollect("ScoreboardElements")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ScoreboardElement

enum class ElementGroup {
    SEPARATOR,
    HEADER,
    MIDDLE,
    FOOTER,
}
