package me.owdding.customscoreboard.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import java.util.Optional

object TextUtils {

    fun String.toComponent() = Component.literal(this)

    val Component.spans: List<Pair<String, Style>>
        get() {
            val output = mutableListOf<Pair<String, Style>>()
            this.visit(
                { style, content ->
                    output.add(content to style)
                    Optional.empty<Unit>()
                },
                Style.EMPTY,
            )
            return output
        }

    fun String.trimIgnoreColor(start: Boolean = true, end: Boolean = true): String {
        return this.tryTrimIgnoreColor(start, end).second
    }

    fun String.tryTrimIgnoreColor(start: Boolean = true, end: Boolean = true): Pair<Boolean, String> {
        var middle = this
        val prefix = StringBuilder()
        val suffix = StringBuilder()

        if (start) {
            var i = 0
            var include = false

            while (i < middle.length) {
                val char = middle[i]
                when {
                    include -> prefix.append(char)
                    char == ChatFormatting.PREFIX_CODE -> {
                        include = true
                        prefix.append(char)
                    }

                    !char.isWhitespace() -> break
                }
                i++
            }

            middle = middle.substring(i)
        }

        if (end) {
            var i = middle.length - 1
            var include = false

            while (i >= 0) {
                val char = middle[i]

                when {
                    include -> suffix.append(char)
                    i - 1 >= 0 && middle[i - 1] == ChatFormatting.PREFIX_CODE -> {
                        include = true
                        suffix.append(char)
                    }

                    !char.isWhitespace() -> break
                }
                i--
            }

            middle = middle.take(i + 1)
        }

        if (middle.isEmpty()) {
            return true to prefix.toString() + suffix.reverse().toString()
        }

        return false to prefix.toString() + middle + suffix.reverse().toString()
    }

    fun Component.trim(): Component {
        val spans = this.spans.toMutableList()
        return when {
            spans.isEmpty() -> Component.empty()
            spans.size == 1 -> Component.literal(spans[0].first.trimIgnoreColor()).setStyle(spans[0].second)
            else -> {
                val prefix = mutableListOf<Component>()
                val suffix = mutableListOf<Component>()

                while (spans.isNotEmpty()) {
                    val (content, style) = spans.removeFirst()
                    val (isEmpty, trimmed) = content.tryTrimIgnoreColor(end = false)
                    prefix.add(Component.literal(trimmed).setStyle(style))

                    if (!isEmpty) break // Means we found actual content
                }

                while (spans.isNotEmpty()) {
                    val (content, style) = spans.removeLast()
                    val (isEmpty, trimmed) = content.tryTrimIgnoreColor(start = false)
                    suffix.addFirst(Component.literal(trimmed).setStyle(style))

                    if (!isEmpty) break // Means we found actual content
                }

                Component.empty().apply {
                    prefix.forEach(this::append)
                    spans.forEach { (content, style) -> this.append(Component.literal(content).setStyle(style)) }
                    suffix.forEach(this::append)
                }
            }
        }
    }

    fun Component.isBlank() = this.string.isBlank()

    fun Regex.anyMatch(input: List<String>) = input.any { this.matches(it) }

}
