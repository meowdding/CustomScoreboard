plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.1"

stonecutter parameters {
    swaps["mod_version"] = "\"" + property("version") + "\";"
    swaps["minecraft"] = "\"" + node.metadata.version + "\";"
    replacements.string {
        direction = eval(current.version, "> 1.21.5")
        replace("// moj_import <", "//!moj_import <")
    }
    stonecutter.versions.forEach { (_, v) ->
        constants["scoreboard_overhaul"] = versionCatalogs.named("libs" + v.replace(".", "")).findLibrary("scoreboard.overhaul").isPresent
    }

    filters.include("**/*.fsh", "**/*.vsh")
    Replacements.read(project).replacements.forEach { (name, replacement) ->
        when (replacement) {
            is StringReplacement -> replacements.string {
                if (replacement.named) {
                    id = name
                }
                direction = eval(current.version, replacement.condition)
                replace(replacement.from, replacement.to)
            }

            is RegexReplacement -> replacements.regex {
                if (replacement.named) {
                    id = name
                }
                direction = eval(current.version, replacement.condition)
                replace(
                    replacement.regex to replacement.to,
                    replacement.reverseRegex to replacement.reverse
                )
            }
        }
    }
}
