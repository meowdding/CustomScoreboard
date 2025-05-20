package me.owdding.customscoreboard

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoElement(val element: ElementGroup = ElementGroup.MIDDLE)

enum class ElementGroup {
    SEPARATOR,
    HEADER,
    MIDDLE,
    FOOTER,
    ;

    companion object {
        fun byName(asString: String?) = ElementGroup.entries.firstOrNull { it.name.equals(asString, true) }?: MIDDLE
    }
}
