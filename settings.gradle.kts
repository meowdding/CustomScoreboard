rootProject.name = "Custom Scoreboard"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.teamresourceful.com/repository/maven-public/")
        maven(url = "https://maven.msrandom.net/repository/cloche")
        maven(url = "https://maven.msrandom.net/repository/root")
        maven("https://api.modrinth.com/maven") {
            content {
                includeGroup("maven.modrinth")
            }
        }

        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
        create("libs1215") {
            from(files("gradle/libs1215.versions.toml"))
        }
        create("libs1218") {
            from(files("gradle/libs1218.versions.toml"))
        }
        create("libs1219") {
            from(files("gradle/libs1219.versions.toml"))
        }
    }
}

include(":annotations")
