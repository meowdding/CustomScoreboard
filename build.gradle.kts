import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loom)
    alias(libs.plugins.ksp)
}

ksp {
    arg("meowdding.modules.project_name", "CustomScoreboard")
    arg("meowdding.modules.package", "me.owdding.customscoreboard.generated")
}

repositories {
    maven("https://maven.teamresourceful.com/repository/maven-public/")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://api.modrinth.com/maven")
}

dependencies {
    compileOnly(libs.meowdding.ktmodules)
    ksp(libs.meowdding.ktmodules)
    compileOnly(project(":annotations"))
    ksp(project(":annotations"))

    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.21.5:2025.04.19@zip")
    })
    modImplementation(libs.loader)
    modImplementation(libs.fabrickotlin)
    modImplementation(libs.fabric)

    modImplementation(libs.hypixelapi)
    modImplementation(libs.skyblockapi)
    modImplementation(libs.rconfig)
    modImplementation(libs.rconfigkt)
    modImplementation(libs.rlib)
    modImplementation(libs.olympus)
    modImplementation(libs.meowdding.lib)
    modImplementation(libs.mixinconstraints)

    include(libs.hypixelapi)
    include(libs.skyblockapi)
    include(libs.rconfig)
    include(libs.rconfigkt)
    include(libs.rlib)
    include(libs.olympus)
    include(libs.meowdding.lib)
    include(libs.mixinconstraints)

    modCompileOnly("maven.modrinth:scoreboard-overhaul:1.4.0-mc1.21.5")

    modRuntimeOnly(libs.devauth)
}

loom {
    runs {
        getByName("client") {
            property("devauth.configDir", rootProject.file(".devauth").absolutePath)
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraft" to libs.versions.minecraft.get(),
                "fabricLoader" to libs.versions.loader.get(),
                "fabricLanguageKotlin" to libs.versions.fabrickotlin.get(),
                "meowddingLib" to libs.versions.meowdding.lib.get(),
                "resourcefullib" to libs.versions.rlib.get(),
                "skyblockApi" to libs.versions.sbapi.get(),
                "olympus" to libs.versions.olympus.get(),
                "resourcefulconfigkt" to libs.versions.rconfigkt.get(),
                "resourcefulconfig" to libs.versions.rconfig.get(),
            )
        }
    }

    jar {
        from("LICENSE")
    }

    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(21)
}
