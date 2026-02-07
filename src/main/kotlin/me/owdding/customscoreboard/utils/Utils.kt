package me.owdding.customscoreboard.utils

import com.teamresourceful.resourcefulconfigkt.api.ConfigDelegateProvider
import com.teamresourceful.resourcefulconfigkt.api.ObservableEntry
import com.teamresourceful.resourcefulconfigkt.api.RConfigKtEntry
import com.teamresourceful.resourcefulconfigkt.api.builders.CategoryBuilder
import me.owdding.ktmodules.AutoCollect
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.profile.effects.EffectsAPI
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import tech.thatgravyboat.skyblockapi.utils.time.until
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object Utils {
    fun hasCookieActive() = EffectsAPI.boosterCookieExpireTime.until() > 1.seconds

    fun Duration.toFormatYears() = buildString {
        val years = inWholeDays / 365
        val days = inWholeDays % 365
        val hours = inWholeHours % 24
        val minutes = inWholeMinutes % 60
        val seconds = inWholeSeconds % 60

        if (years > 0) append("${years}y ")
        if (days > 0) append("${days}d ")
        if (hours > 0) append("${hours}h ")
        if (minutes > 0) append("${minutes}m ")
        if (years <= 0 && days <= 0 && seconds > 0) append("${seconds}s") // Only show seconds if there is no days or years
        if (isEmpty()) append("0s")
    }.trim()

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

    fun String.moulConfigColor(): Int = split(":").map { part -> part.toInt() }.let {
        (it[1] shl 24) or (it[2] shl 16) or (it[3] shl 8) or it[4]
    }

    fun <T> CategoryBuilder.observable(entry: ConfigDelegateProvider<RConfigKtEntry<T>>, onChange: () -> Unit) = this.observable(entry) { _, _ -> onChange() }
    fun <T> ConfigDelegateProvider<RConfigKtEntry<T>>.observable(onChange: (T, T) -> Unit) = ObservableEntry(this, onChange)
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
